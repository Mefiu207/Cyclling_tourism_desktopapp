package com.project.ui.buttons;

import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.services.AbstractServices;
import com.project.springbootjavafx.services.CenyService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The {@code RightCenyButton} class is a specialized button that displays pricing information
 * associated with a selected tour type (TypyWycieczek) when clicked.
 *
 * <p>
 * When this button is clicked, it opens a dialog that prompts the user to select a tour type.
 * After the user selects a tour type, it retrieves the corresponding pricing data (Ceny) via
 * the {@link CenyService} and displays the information in a {@link TableView} in the main content area.
 * </p>
 *
 * <p>
 * The button is constructed using a left sidebar button (an instance of {@link CustomLeftButton})
 * from which it derives the {@link AbstractServices} for tour types. The required services and the main
 * content component are retrieved from the Spring context via {@link SpringContextHolder}.
 * </p>
 *
 * <p>
 * <b>Note:</b> This class does not have any Spring annotations itself, but it relies on services obtained from the Spring context.
 * </p>
 */
public class RightCenyButton extends Button {

    /**
     * Service handling operations for tour types.
     * This is obtained from the associated left button.
     */
    private AbstractServices typyWycieczekService;

    /**
     * Service for handling price (Ceny) related operations.
     */
    private CenyService cenyService;

    /**
     * Constructs a new {@code RightCenyButton} with the specified name and associated left button.
     * It initializes the required services and sets up the action handler.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button from which the tour type service is derived
     */
    public RightCenyButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.typyWycieczekService = leftButton.getServices();
        this.cenyService = SpringContextHolder.getContext().getBean(CenyService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the button click event.
     *
     * <p>
     * This method performs the following steps:
     * </p>
     * <ol>
     *   <li>Retrieves the {@link MainContent} bean from the Spring context.</li>
     *   <li>Creates a dialog to prompt the user to select a tour type (TypyWycieczek) from a ComboBox.</li>
     *   <li>Converts the dialog result to a selected tour type.</li>
     *   <li>Uses the {@link CenyService} to find the pricing data (Ceny) for the selected tour type.</li>
     *   <li>If no pricing data is available, displays an information alert.</li>
     *   <li>If pricing data is available, creates a {@link TableView} with columns for various pricing attributes.</li>
     *   <li>Populates the table with the pricing data and updates the main content area with the table view.</li>
     * </ol>
     */
    private void onClick() {
        // Retrieve MainContent from Spring context
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Create a dialog for selecting a tour type
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Wybierz Typ Wycieczki");
        dialog.setHeaderText("Wybierz typ wycieczki, dla którego chcesz zobaczyć ceny.");

        // Set up dialog buttons
        ButtonType viewPricesButtonType = new ButtonType("Zobacz Ceny", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(viewPricesButtonType, ButtonType.CANCEL);

        // Create a ComboBox for available tour types
        ComboBox<TypyWycieczek> typyWycieczekComboBox = new ComboBox<>();
        typyWycieczekComboBox.setItems(FXCollections.observableArrayList(typyWycieczekService.getAll()));
        typyWycieczekComboBox.setPromptText("Wybierz Typ Wycieczki");

        // Add the ComboBox to a VBox and set it as the dialog content
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(typyWycieczekComboBox);
        dialog.getDialogPane().setContent(content);

        // Request focus on the ComboBox when the dialog is shown
        Platform.runLater(() -> typyWycieczekComboBox.requestFocus());

        // Convert the dialog result to the selected tour type
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == viewPricesButtonType) {
                return typyWycieczekComboBox.getValue();
            }
            return null;
        });

        // Process the dialog result
        dialog.showAndWait().ifPresent(selectedTyp -> {
            if (selectedTyp != null) {
                try {
                    Ceny cenyList = cenyService.findByTypWycieczki(selectedTyp);

                    if (cenyList == null) {
                        showAlert(Alert.AlertType.INFORMATION, "Brak Danych", "Dla wybranego typu wycieczki nie ma dostępnych cen.");
                        return;
                    }

                    // Create a TableView for displaying pricing information
                    TableView<Ceny> cenyTableView = new TableView<>();

                    // Define columns for the TableView
                    TableColumn<Ceny, BigDecimal> pok1Column = new TableColumn<>("Pokój 1");
                    pok1Column.setCellValueFactory(new PropertyValueFactory<>("pok_1"));

                    TableColumn<Ceny, BigDecimal> pok2Column = new TableColumn<>("Pokój 2");
                    pok2Column.setCellValueFactory(new PropertyValueFactory<>("pok_2"));

                    TableColumn<Ceny, BigDecimal> pok3Column = new TableColumn<>("Pokój 3");
                    pok3Column.setCellValueFactory(new PropertyValueFactory<>("pok_3"));

                    TableColumn<Ceny, BigDecimal> pok4Column = new TableColumn<>("Pokój 4");
                    pok4Column.setCellValueFactory(new PropertyValueFactory<>("pok_4"));

                    TableColumn<Ceny, Integer> ulgaDzieckoColumn = new TableColumn<>("Ulga Dziecko (%)");
                    ulgaDzieckoColumn.setCellValueFactory(new PropertyValueFactory<>("ulga_dziecko"));

                    TableColumn<Ceny, BigDecimal> rowerColumn = new TableColumn<>("Rower");
                    rowerColumn.setCellValueFactory(new PropertyValueFactory<>("rower"));

                    TableColumn<Ceny, BigDecimal> eBikeColumn = new TableColumn<>("E-Bike");
                    eBikeColumn.setCellValueFactory(new PropertyValueFactory<>("e_bike"));

                    TableColumn<Ceny, BigDecimal> dodatkowaNocColumn = new TableColumn<>("Dodatkowa Noc");
                    dodatkowaNocColumn.setCellValueFactory(new PropertyValueFactory<>("dodatkowa_noc"));

                    TableColumn<Ceny, BigDecimal> hbColumn = new TableColumn<>("HB");
                    hbColumn.setCellValueFactory(new PropertyValueFactory<>("hb"));

                    // Add columns to the TableView
                    cenyTableView.getColumns().addAll(
                            pok1Column, pok2Column, pok3Column, pok4Column,
                            ulgaDzieckoColumn, rowerColumn, eBikeColumn, dodatkowaNocColumn, hbColumn
                    );

                    // Set data into the TableView
                    cenyTableView.setItems(FXCollections.observableArrayList(cenyList));

                    // Optionally adjust column widths
                    cenyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Update the main content area with the TableView
                    mainContent.updateContent(cenyTableView);
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Typ jest null", e.getMessage());
                }
            }
        });
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures the alert is shown on the JavaFX Application Thread by using
     * {@link Platform#runLater(Runnable)}.
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
