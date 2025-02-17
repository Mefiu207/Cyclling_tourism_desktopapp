package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.WrongLetterException;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.services.KlienciService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.services.WycieczkiService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code AddPokojButton} class extends {@link Button} and provides functionality to add a new room (Pokoje)
 * for a selected trip (Wycieczki). This button, when clicked, opens a dialog where the user selects a trip via
 * an editable ComboBox with auto-completion and enters the room type. Based on the room type, the room capacity
 * is determined and a new {@link Pokoje} object is created and added to the database.
 *
 * <p>
 * After successfully adding the room, the left sidebar is refreshed by calling {@code onClick()} on the associated
 * {@link CustomLeftButton} instance.
 * </p>
 *
 * <p>
 * The required services ({@link WycieczkiService}, {@link PokojeService}, and {@link KlienciService})
 * are retrieved from the Spring context via {@link SpringContextHolder}.
 * </p>
 */
public class AddPokojButton extends Button {

    /**
     * Service used to manage room (Pokoje) related operations.
     */
    private final PokojeService pokojeService;

    /**
     * Service used to manage trip (Wycieczki) related operations.
     */
    private final WycieczkiService wycieczkiService;

    /**
     * Service used to manage client (Klienci) related operations.
     */
    private final KlienciService klienciServices;

    /**
     * Reference to the left sidebar button used to refresh the view after adding a room.
     */
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddPokojButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button providing context and services for room operations
     */
    public AddPokojButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.leftButton = leftButton;
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.klienciServices = SpringContextHolder.getContext().getBean(KlienciService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);

        // Set the action to execute when the button is clicked.
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the button click event to add a new room.
     *
     * <p>
     * This method opens a dialog that allows the user to select a trip (Wycieczki) using an editable ComboBox
     * with auto-completion and to enter the room type via a text field. Based on the first character of the room type,
     * the room capacity is determined:
     * </p>
     * <ul>
     *   <li>'e' → capacity 1</li>
     *   <li>'d' → capacity 2</li>
     *   <li>'t' → capacity 3</li>
     *   <li>'q' → capacity 4</li>
     * </ul>
     *
     * <p>
     * If the dialog is confirmed and all fields are valid, a new {@link Pokoje} object is created with the selected trip,
     * room type, capacity, and zero clients. The room is then added using {@link PokojeService}. Upon success, a confirmation
     * alert is shown and the left sidebar is refreshed.
     * </p>
     */
    public void onClick() {
        // Obtain the MainContent instance from the Spring context
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Create a dialog for adding a new room.
        Dialog<Pokoje> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nowy Pokój");
        dialog.setHeaderText("Wprowadź dane nowego pokoju.");

        // Define dialog buttons.
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create a grid layout for the form.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Create an editable ComboBox for selecting a trip (Wycieczki) with auto-completion.
        Label wycieczkaLabel = new Label("Wycieczka:");
        ComboBox<Wycieczki> wycieczkiComboBox = new ComboBox<>();
        wycieczkiComboBox.setEditable(true);
        List<Wycieczki> wycieczkiList = wycieczkiService.getAll();
        ObservableList<Wycieczki> observableWycieczki = FXCollections.observableArrayList(wycieczkiList);
        wycieczkiComboBox.setItems(observableWycieczki);
        wycieczkiComboBox.setPromptText("Wybierz lub wpisz wycieczkę");

        // Set a converter to properly display trip names.
        wycieczkiComboBox.setConverter(new StringConverter<Wycieczki>() {
            @Override
            public String toString(Wycieczki wycieczki) {
                return wycieczki != null ? wycieczki.toString() : "";
            }

            @Override
            public Wycieczki fromString(String string) {
                return wycieczkiList.stream()
                        .filter(w -> w.toString().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Implement auto-completion for the trip ComboBox.
        wycieczkiComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                wycieczkiComboBox.hide();
                wycieczkiComboBox.setItems(observableWycieczki);
            } else {
                List<Wycieczki> filtered = wycieczkiList.stream()
                        .filter(wycieczka -> wycieczka.toString().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());
                wycieczkiComboBox.setItems(FXCollections.observableArrayList(filtered));
                wycieczkiComboBox.show();
            }
        });

        // Create a text field for entering the room type.
        Label typPokojuLabel = new Label("Typ Pokoju:");
        TextField typPokojuField = new TextField();
        typPokojuField.setPromptText("Typ Pokoju");

        // Add the controls to the grid.
        grid.add(wycieczkaLabel, 0, 0);
        grid.add(wycieczkiComboBox, 1, 0);
        grid.add(typPokojuLabel, 0, 1);
        grid.add(typPokojuField, 1, 1);

        // Set the grid as the dialog content.
        dialog.getDialogPane().setContent(grid);

        // Set initial focus on the trip ComboBox.
        Platform.runLater(wycieczkiComboBox::requestFocus);

        // Convert the dialog result to a Pokoje object.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Wycieczki selectedWycieczka = wycieczkiComboBox.getValue();
                String typPokoju = typPokojuField.getText().trim();

                // Validate that all fields are filled.
                if (selectedWycieczka == null || typPokoju.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return null;
                }

                // Create a new Pokoje instance.
                Pokoje pokoj = new Pokoje();
                pokoj.setWycieczka(selectedWycieczka);
                pokoj.setTypPokoju(typPokoju);
                pokoj.setListaHoteli(false);

                // Determine the room capacity based on the first character of the room type.
                Integer pokojRozmiar = 0;
                switch (pokoj.getTypPokoju().charAt(0)) {
                    case 'e':
                        pokojRozmiar = 1;
                        break;
                    case 'd':
                        pokojRozmiar = 2;
                        break;
                    case 't':
                        pokojRozmiar = 3;
                        break;
                    case 'q':
                        pokojRozmiar = 4;
                        break;
                }
                pokoj.setIlMiejsc(pokojRozmiar);
                pokoj.setIlKlientow(0);
                return pokoj;
            }
            return null;
        });

        // Show the dialog and process the result.
        dialog.showAndWait().ifPresent(pokoj -> {
            try {
                // Add the new room to the database.
                pokojeService.add(pokoj);
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Pokój został dodany pomyślnie.");
                leftButton.onClick();
            } catch (WrongLetterException e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", e.getMessage());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas zapisywania pokoju: " + e.getMessage());
            }
        });
    }

    /**
     * Displays an alert dialog with the specified alert type, title, and message.
     *
     * <p>
     * This method ensures that the alert is displayed on the JavaFX Application Thread by using {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of alert to display
     * @param title     the title of the alert dialog
     * @param message   the message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
