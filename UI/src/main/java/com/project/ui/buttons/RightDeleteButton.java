package com.project.ui.buttons;

import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import com.project.springbootjavafx.services.AbstractServices;
import javafx.scene.layout.GridPane;

/**
 * The {@code RightDeleteButton} class represents a button used for deleting records
 * from a table. It is intended to be placed in the right sidebar and works in conjunction
 * with a corresponding left sidebar button (an instance of {@link CustomLeftButton})
 * that provides access to the service managing the entity records.
 *
 * <p>
 * When this button is clicked, it displays a dialog prompting the user to enter the ID (or name)
 * of the record to delete. The button then validates the input, checks whether the record exists
 * using the provided service, and if so, attempts to delete it. After a successful deletion, the
 * associated left button's {@code onClick()} method is invoked to refresh the table view, and
 * the main content view is scrolled to the bottom.
 * </p>
 *
 * @param <T>  the entity type associated with the record
 * @param <ID> the type of the record's identifier (e.g., {@link String} or {@link Integer})
 */
public class RightDeleteButton<T, ID> extends Button {

    /**
     * The associated left sidebar button that holds the reference to the service and is used
     * to refresh the table view after deletion.
     */
    protected CustomLeftButton<T, ID> leftButton;

    /**
     * The service used to manage the entity records.
     */
    protected AbstractServices services;

    /**
     * The class of the identifier (ID) as used by the service. This is used to properly parse
     * the user input before deletion.
     */
    protected Class<ID> clazz;

    /**
     * Constructs a new {@code RightDeleteButton} with the specified text.
     * This constructor is available if no associated left button is provided.
     *
     * @param text the text to display on the button
     */
    public RightDeleteButton(String text) {
        super(text);
    }

    /**
     * Constructs a new {@code RightDeleteButton} with the specified text and associated left button.
     * It initializes the service and the ID class from the provided left button.
     *
     * @param text       the text to display on the button
     * @param leftButton the associated left sidebar button that provides the service reference
     */
    public RightDeleteButton(String text, CustomLeftButton<T, ID> leftButton) {
        super(text);
        this.leftButton = leftButton;
        this.services = leftButton.getServices();
        // Retrieve the class type of the ID from the service to allow proper deletion.
        this.clazz = services.getIdClass();
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the delete button click event.
     *
     * <p>
     * When the button is clicked, this method displays a dialog prompting the user to enter the ID (or name)
     * of the record to delete. It then performs the following actions:
     * </p>
     * <ol>
     *   <li>Validates that the ID field is not empty.</li>
     *   <li>Checks if a record with the specified ID exists using the associated service.</li>
     *   <li>If the record exists, attempts to delete it; otherwise, displays an error alert.</li>
     *   <li>Upon successful deletion, refreshes the table view by invoking the {@code onClick()} method
     *       of the associated left sidebar button and scrolls the main content view to the bottom.</li>
     * </ol>
     */
    protected void onClick() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Usuń rekord z tabeli ");
        dialog.setHeaderText("ID (lub nazwa) obiektu do usunięcia (pierwsza kolumna)");

        ButtonType deleteButtonType = new ButtonType("Usuń", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("Id");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(idField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButtonType) {
                return idField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(id -> {
            if (id.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "ID nie może być puste.");
                return;
            }

            if (!services.existsById(id)) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Rekord o dany ID nie istnieje");
                return;
            }
            try {
                if (clazz == String.class) {
                    services.delete(id);
                } else {
                    services.delete(Integer.valueOf(id));
                }
                // Record deletion successful.
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Rekord został usunięty.");

                // Refresh the table view in the left sidebar.
                leftButton.onClick();
                scrollDonw();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się usunąć rekordu: " + e.getMessage());
            }
        });
    }

    /**
     * Scrolls the main content view to the bottom.
     *
     * <p>
     * This method retrieves the {@link MainContent} bean from the Spring context and calls its
     * {@code scrollToBottom()} method to ensure that the most recent changes are visible.
     * </p>
     */
    private void scrollDonw() {
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);
        mainContent.scrollToBottom();
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
