package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.Hotele;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.HoteleService;
import com.project.springbootjavafx.services.MiastaService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * The {@code AddHoteleButton} class extends {@link Button} and provides functionality for adding a new hotel.
 *
 * <p>
 * When this button is clicked, it opens a dialog that prompts the user to enter details for a new hotel,
 * including the hotel code, name, city, address, email, and phone number. The dialog uses a form with input fields
 * and validates that all fields are filled. Upon successful submission, a new {@link Hotele} object is created and added
 * using the {@link HoteleService}. If the hotel is added successfully, the left sidebar is refreshed and the main content
 * area scrolls to the bottom. In case of errors (such as wrong code length or duplicate hotel), an error alert is displayed.
 * </p>
 *
 * <p>
 * Dependencies are obtained from the Spring context via {@link SpringContextHolder}.
 * </p>
 */
public class AddHoteleButton extends Button {

    /**
     * Service for handling hotel-related operations.
     */
    private final HoteleService hoteleService;

    /**
     * Service for handling city-related operations.
     */
    private final MiastaService miastaService;

    /**
     * The associated left sidebar button that is used to refresh the view after a hotel is added.
     */
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddHoteleButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button providing context and services for hotel operations
     */
    public AddHoteleButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.leftButton = leftButton;
        this.hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);
        this.miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);
        this.setOnAction(e -> openAddHotelDialog());
    }

    /**
     * Opens a dialog for adding a new hotel.
     *
     * <p>
     * The dialog prompts the user to enter the hotel code, name, select a city from a ComboBox, and enter the address,
     * email, and phone number. The form performs validation to ensure that none of the fields are empty.
     * When the user submits valid data, a new {@link Hotele} instance is created and added using the {@link HoteleService}.
     * After a successful addition, the left sidebar is refreshed and the main content area is scrolled to the bottom.
     * In case of errors during hotel addition (such as a wrong code length or a duplicate entry), an error alert is shown.
     * </p>
     */
    public void openAddHotelDialog() {
        // Create the dialog for adding a new hotel
        Dialog<Hotele> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Hotel");
        dialog.setHeaderText("Wprowadź informacje o nowym hotelu");

        // Set up dialog buttons
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create a grid for the form layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Form fields
        TextField kodField = new TextField();
        kodField.setPromptText("Kod hotelu (6 znaków)");

        TextField nazwaField = new TextField();
        nazwaField.setPromptText("Nazwa hotelu");

        ComboBox<Miasta> miastoComboBox = new ComboBox<>();
        miastoComboBox.setPromptText("Wybierz miasto");
        miastoComboBox.setItems(FXCollections.observableArrayList(miastaService.getAll()));

        TextField adresField = new TextField();
        adresField.setPromptText("Adres");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon");

        // Add form fields to the grid
        grid.add(new Label("Kod:"), 0, 0);
        grid.add(kodField, 1, 0);

        grid.add(new Label("Nazwa:"), 0, 1);
        grid.add(nazwaField, 1, 1);

        grid.add(new Label("Miasto:"), 0, 2);
        grid.add(miastoComboBox, 1, 2);

        grid.add(new Label("Adres:"), 0, 3);
        grid.add(adresField, 1, 3);

        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        grid.add(new Label("Telefon:"), 0, 5);
        grid.add(telefonField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Validate the Add button: disable it until all fields are filled
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Add listeners to form fields for validation
        kodField.textProperty().addListener((observable, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        nazwaField.textProperty().addListener((observable, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        miastoComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        adresField.textProperty().addListener((observable, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        emailField.textProperty().addListener((observable, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        telefonField.textProperty().addListener((observable, oldValue, newValue) ->
                validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));

        // Convert the dialog result to a Hotele object
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String kod = kodField.getText().trim();
                String nazwa = nazwaField.getText().trim();
                Miasta miasto = miastoComboBox.getValue();
                String adres = adresField.getText().trim();
                String email = emailField.getText().trim();
                String telefon = telefonField.getText().trim();
                return new Hotele(kod, nazwa, miasto, adres, email, telefon);
            }
            return null;
        });

        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(hotele -> {
            try {
                hoteleService.add(hotele);
                leftButton.onClick();
                mainContent.scrollToBottom();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Hotel został dodany pomyślnie!");
                alert.showAndWait();
            } catch (WrongCodeLengthException ex) {
                exceptionAlert(ex.getMessage());
            } catch (DuplicatedEntityExceptionn ex) {
                exceptionAlert(ex.getMessage());
            } catch (Exception e) {
                exceptionAlert("Nieznany błąd");
            }
        });
    }

    /**
     * Displays an error alert with the given message.
     *
     * @param msg the error message to display
     */
    private void exceptionAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie udało się dodać hotelu");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /**
     * Validates the form fields and enables/disables the add button accordingly.
     *
     * @param kodField      the TextField for the hotel code
     * @param nazwaField    the TextField for the hotel name
     * @param miastoComboBox the ComboBox for selecting a city
     * @param adresField    the TextField for the address
     * @param emailField    the TextField for the email
     * @param telefonField  the TextField for the phone number
     * @param addButton     the button to add the hotel (will be enabled if all fields are valid)
     */
    private void validateForm(TextField kodField, TextField nazwaField, ComboBox<Miasta> miastoComboBox,
                              TextField adresField, TextField emailField, TextField telefonField, Node addButton) {
        boolean disable = kodField.getText().trim().isEmpty() ||
                nazwaField.getText().trim().isEmpty() ||
                miastoComboBox.getSelectionModel().isEmpty() ||
                adresField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                telefonField.getText().trim().isEmpty();
        addButton.setDisable(disable);
    }
}
