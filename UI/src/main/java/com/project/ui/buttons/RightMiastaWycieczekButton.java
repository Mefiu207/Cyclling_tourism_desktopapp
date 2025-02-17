package com.project.ui.buttons;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.services.AbstractServices;
import com.project.springbootjavafx.services.MiastaWycieczekService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * The {@code RightMiastaWycieczekButton} class represents a specialized button located in the right sidebar
 * that facilitates the display of cities (MiastaWycieczek) associated with a selected tour type (TypyWycieczek).
 *
 * <p>
 * When this button is clicked, it opens a dialog that prompts the user to select a tour type from a ComboBox.
 * After a tour type is selected, the button uses {@link MiastaWycieczekService} to retrieve the list of
 * associated cities. These results are then displayed in a {@link TableView} with two columns: one for the city name
 * and one for the night number. Finally, the generated TableView is set as the main content of the application.
 * </p>
 *
 * <p>
 * The required services and the main content component are retrieved from the Spring context via
 * {@link SpringContextHolder}. The tour type service is obtained from the left sidebar button that is passed to the constructor.
 * </p>
 */
public class RightMiastaWycieczekButton extends Button {

    private AbstractServices typyWycieczekService;
    private MiastaWycieczekService miastaWycieczekService;

    /**
     * Constructs a new {@code RightMiastaWycieczekButton} with the specified text and reference to a left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton a reference to a {@link CustomLeftButton} that provides the tour type service
     */
    public RightMiastaWycieczekButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.typyWycieczekService = leftButton.getServices();
        this.miastaWycieczekService = SpringContextHolder.getContext().getBean(MiastaWycieczekService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the button click event.
     *
     * <p>
     * When the button is clicked, the following actions occur:
     * </p>
     * <ol>
     *   <li>A dialog is displayed prompting the user to select a tour type (TypyWycieczek) via a ComboBox.</li>
     *   <li>If a tour type is selected, the {@link MiastaWycieczekService} is used to retrieve a list of
     *       {@link MiastaWycieczek} associated with the selected tour type.</li>
     *   <li>If no cities are associated with the tour type, an informational alert is displayed.</li>
     *   <li>If the list is non-empty, a {@link TableView} is created with two columns: one for the city name
     *       and one for the night number (Numer Nocy).</li>
     *   <li>The TableView is then populated with the retrieved data and set as the main content of the application
     *       via {@link MainContent#updateContent(javafx.scene.control.TableView)}.</li>
     * </ol>
     */
    private void onClick() {
        // Retrieve the main content component from the Spring context
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Create a dialog to prompt the user to select a tour type (TypyWycieczek)
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Wybierz Typ Wycieczki");
        dialog.setHeaderText("Wybierz typ wycieczki, dla którego chcesz zobaczyć miasta.");

        // Set dialog buttons
        ButtonType viewCitiesButtonType = new ButtonType("Zobacz Miasta", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(viewCitiesButtonType, ButtonType.CANCEL);

        // Create a ComboBox populated with tour types from the service
        ComboBox<TypyWycieczek> typyWycieczekComboBox = new ComboBox<>();
        typyWycieczekComboBox.setItems(FXCollections.observableArrayList(typyWycieczekService.getAll()));
        typyWycieczekComboBox.setPromptText("Wybierz Typ Wycieczki");

        // Add the ComboBox to a VBox and set it as the dialog content
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(typyWycieczekComboBox);
        dialog.getDialogPane().setContent(content);

        // Set focus to the ComboBox when the dialog is displayed
        Platform.runLater(() -> typyWycieczekComboBox.requestFocus());

        // Convert the dialog result to the selected tour type
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == viewCitiesButtonType) {
                return typyWycieczekComboBox.getValue();
            }
            return null;
        });

        // Process the dialog result
        dialog.showAndWait().ifPresent(selectedTyp -> {
            if (selectedTyp != null) {
                try {
                    // Retrieve the list of cities associated with the selected tour type
                    List<MiastaWycieczek> miastaWycieczekList = miastaWycieczekService.findByTypWycieczki(selectedTyp);

                    if (miastaWycieczekList == null || miastaWycieczekList.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "Brak Danych", "Dla wybranego typu wycieczki nie ma przypisanych miast.");
                        return;
                    }

                    // Create a TableView to display the city data
                    TableView<MiastaWycieczek> miastaTableView = new TableView<>();

                    // Create a column for the city name
                    TableColumn<MiastaWycieczek, String> miastoColumn = new TableColumn<>("Miasto");
                    miastoColumn.setCellValueFactory(cellData -> {
                        if (cellData.getValue().getMiasta() != null) {
                            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMiasta().getMiasto());
                        } else {
                            return new javafx.beans.property.SimpleStringProperty("Brak");
                        }
                    });

                    // Create a column for the night number
                    TableColumn<MiastaWycieczek, Integer> numerNocyColumn = new TableColumn<>("Numer Nocy");
                    numerNocyColumn.setCellValueFactory(new PropertyValueFactory<>("numerNocy"));

                    // Add columns to the TableView
                    miastaTableView.getColumns().addAll(miastoColumn, numerNocyColumn);

                    // Populate the TableView with the retrieved data
                    miastaTableView.setItems(FXCollections.observableArrayList(miastaWycieczekList));

                    // Optionally, adjust column widths to fit the TableView
                    miastaTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Update the main content area with the TableView
                    mainContent.updateContent(miastaTableView);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas pobierania danych: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures that the alert is displayed on the JavaFX Application Thread by using
     * {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of alert to display
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
