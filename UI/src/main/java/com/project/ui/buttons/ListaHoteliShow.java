package com.project.ui.buttons;

import com.project.springbootjavafx.models.ListaNocyHoteli;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.ListaNocyHoteliService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * The {@code ListaHoteliShow} class extends {@link Button} and is used to display a table
 * of hotel night lists associated with a specific room.
 *
 * <p>
 * When this button is clicked, it prompts the user to select a room by entering the room's ID.
 * It then retrieves the corresponding hotel night list data for that room using the
 * {@link ListaNocyHoteliService} and displays the data in a {@link TableView} within the main content area.
 * </p>
 *
 * <p>
 * The required services and main content component are obtained from the Spring context via
 * {@link SpringContextHolder}.
 * </p>
 */
public class ListaHoteliShow extends Button {

    /**
     * Service used for room-related operations.
     */
    private final PokojeService pokojeService;

    /**
     * Service used for hotel night list operations.
     */
    private final ListaNocyHoteliService listaNocyHoteliService;

    /**
     * The main content area of the application where the table view will be displayed.
     */
    private final MainContent mainContent;

    /**
     * Constructs a new {@code ListaHoteliShow} button with the specified text.
     * It retrieves the required services and the main content component from the Spring context.
     *
     * @param name the text to display on the button
     */
    public ListaHoteliShow(String name){
        super(name);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.mainContent = SpringContextHolder.getContext().getBean(MainContent.class);
        this.listaNocyHoteliService = SpringContextHolder.getContext().getBean(ListaNocyHoteliService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the button click event.
     *
     * <p>
     * The method performs the following steps:
     * </p>
     * <ol>
     *   <li>Prompts the user to select a room by invoking {@link #wybierzPokoj()}.</li>
     *   <li>If a room is selected, retrieves the list of hotel nights for that room using
     *       {@link ListaNocyHoteliService}.</li>
     *   <li>If no data is available, displays an information alert.</li>
     *   <li>Creates a {@link TableView} and configures its columns for "Noc" (Night), "Data" (Date), "Miasto" (City),
     *       and "Hotel".</li>
     *   <li>Populates the table with the retrieved data and updates the main content area.</li>
     * </ol>
     */
    public void onClick() {
        Pokoje pokoj = wybierzPokoj();
        if (pokoj == null) return;

        // Retrieve hotel night list data for the selected room
        List<ListaNocyHoteli> listaNocyHoteli = listaNocyHoteliService.getListyByPokoj(pokoj);

        if (listaNocyHoteli.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak danych", "Dla wybranego pokoju nie ma danych do wyświetlenia.");
            return;
        }

        // Create TableView and configure its columns
        TableView<ListaNocyHoteli> tableView = new TableView<>();

        // "Noc" (Night) column
        TableColumn<ListaNocyHoteli, Integer> columnNoc = new TableColumn<>("Noc");
        columnNoc.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNoc()));

        // "Data" (Date) column
        TableColumn<ListaNocyHoteli, String> columnData = new TableColumn<>("Data");
        columnData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getData().toString()
        ));

        // "Miasto" (City) column
        TableColumn<ListaNocyHoteli, String> columnMiasto = new TableColumn<>("Miasto");
        columnMiasto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMiasto()));

        // "Hotel" column
        TableColumn<ListaNocyHoteli, String> columnHotel = new TableColumn<>("Hotel");
        columnHotel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHotel()));

        // Add columns to the table view
        tableView.getColumns().addAll(columnNoc, columnData, columnMiasto, columnHotel);

        // Populate the table with data
        tableView.getItems().addAll(listaNocyHoteli);

        // Update the main content area with the table view
        mainContent.updateContent(tableView);
    }

    /**
     * Prompts the user to select a room by displaying a dialog.
     *
     * <p>
     * The dialog requests the room ID and attempts to retrieve the corresponding {@link Pokoje} object
     * using {@link PokojeService}. If the room does not exist or does not have an associated hotel list,
     * an alert is shown. Additionally, if the room is not fully occupied, an informational alert is displayed.
     * </p>
     *
     * @return the selected {@link Pokoje} object, or {@code null} if no valid room is selected
     */
    private Pokoje wybierzPokoj(){
        Dialog<Pokoje> dialog = new Dialog<>();
        dialog.setTitle("Wybierz pokój");
        dialog.setHeaderText("Podaj Id pokoju którego listę hoteli chcesz wyświetlić");

        ButtonType addButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField pokojID = new TextField();
        pokojID.setPromptText("ID pokoju");

        grid.add(new Label("ID pokoju:"), 0, 1);
        grid.add(pokojID, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the text field when the dialog is shown
        Platform.runLater(pokojID::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String ID = pokojID.getText().trim();
                try {
                    return pokojeService.getById(Integer.valueOf(ID));
                } catch (NoSuchElementException e) {
                    showAlert(Alert.AlertType.ERROR, "Brak obiektu", "Nie ma pokoju o takim ID");
                }
            }
            return null;
        });

        Optional<Pokoje> result = dialog.showAndWait();
        if(result.isPresent()) {
            if (!result.get().getListaHoteli()) {
                showAlert(Alert.AlertType.ERROR, "Brak listy hoteli", "Najpierw wybierz hotele dla pokoju");
                return null;
            }

            if(result.get().getIlKlientow() < result.get().getIlMiejsc()){
                showAlert(Alert.AlertType.INFORMATION, "Uwaga", "Na ten moment ten pokój nie jest zapełniony");
            }
        }
        return result.orElse(null);
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures that the alert is shown on the JavaFX Application Thread by using
     * {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of the alert
     * @param title     the title of the alert dialog
     * @param message   the content message of the alert dialog
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
