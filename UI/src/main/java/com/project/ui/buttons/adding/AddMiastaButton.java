package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.MiastaService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * The {@code AddMiastaButton} class extends {@link Button} and provides functionality to add a new city
 * (Miasta) to the system.
 *
 * <p>
 * When this button is clicked, a dialog is displayed prompting the user to enter the name of a new city.
 * The entered city is then added using {@link MiastaService}. Upon successful addition, the left sidebar is
 * refreshed and the main content area scrolls to the bottom. If the city already exists, an error alert is shown.
 * </p>
 *
 * <p>
 * The required service ({@link MiastaService}) and a reference to a left sidebar button (of type {@link CustomLeftButton})
 * are obtained from the Spring context via {@link SpringContextHolder}.
 * </p>
 */
public class AddMiastaButton extends Button {

    /**
     * Service used for city-related operations.
     */
    private MiastaService miastaService;

    /**
     * A reference to the left sidebar button used to refresh the view after a new city is added.
     */
    private CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddMiastaButton} with the specified text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button providing context and services for city operations
     */
    public AddMiastaButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.leftButton = leftButton;
        this.miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);

        // Set the action for the button click to open the add city dialog.
        this.setOnAction(e -> openAddMiastoDialog());
    }

    /**
     * Opens a dialog to add a new city.
     *
     * <p>
     * This method displays a dialog that prompts the user to enter the name of a new city.
     * If the user confirms the dialog, a new {@link Miasta} object is created and added using
     * {@link MiastaService}. Upon successful addition, the left sidebar is refreshed and the main content area
     * scrolls to the bottom. In case the city already exists, an error alert is displayed.
     * </p>
     */
    private void openAddMiastoDialog() {
        // Obtain the MainContent bean from the Spring context.
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Create a dialog to input a new city name.
        Dialog<Miasta> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Miasto");
        dialog.setHeaderText("Wprowadź nowe miasta:");

        // Set up dialog buttons.
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create a grid for the form layout.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create a text field for entering the city name.
        TextField miastoField = new TextField();
        miastoField.setPromptText("Miasto");

        // Add label and text field to the grid.
        grid.add(new Label("Miasto:"), 0, 1);
        grid.add(miastoField, 1, 1);

        // Set the grid as the dialog content.
        dialog.getDialogPane().setContent(grid);

        // Request focus on the text field when the dialog is displayed.
        Platform.runLater(miastoField::requestFocus);

        // Convert the dialog result to a Miasta object.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String miasto = miastoField.getText().trim();
                return new Miasta(miasto);
            }
            return null;
        });

        // Show the dialog and wait for the result.
        Optional<Miasta> result = dialog.showAndWait();

        // Process the result if present.
        result.ifPresent(miastoObj -> {
            try {
                miastaService.add(miastoObj);
                leftButton.onClick();
                mainContent.scrollToBottom();
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Miasto zostało dodane.");
            } catch (DuplicatedEntityExceptionn ex) {
                showAlert(Alert.AlertType.ERROR, "Błąd", ex.getMessage());
            }
        });
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method uses {@link Alert} to display a message to the user and ensures that the alert
     * is shown on the JavaFX Application Thread using {@link Platform#runLater(Runnable)}.
     * </p>
     *
     * @param alertType the type of alert to display
     * @param title     the title of the alert dialog
     * @param message   the message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
