package com.project.ui.buttons.adding;

import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.services.AbstractServices;
import com.project.springbootjavafx.services.TypyWycieczekService;
import com.project.springbootjavafx.services.WycieczkiService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The {@code AddWycieczkiButton} class extends {@link Button} and provides functionality for adding a new trip.
 *
 * <p>
 * When this button is clicked, it opens a dialog that allows the user to enter details for a new trip.
 * The user must provide:
 * </p>
 * <ul>
 *   <li>The trip name via a text field.</li>
 *   <li>A trip type via a combo box, populated with available {@link TypyWycieczek} entries.</li>
 *   <li>A start date via a {@link DatePicker}.</li>
 * </ul>
 *
 * <p>
 * The end date is automatically calculated by adding the number of nights (as defined in the selected trip type)
 * to the start date. If all fields are valid, a new {@link Wycieczki} object is created and added via
 * {@link WycieczkiService}. Upon successful addition, a success alert is displayed and the left sidebar is refreshed
 * by calling {@code onClick()} on the associated {@link CustomLeftButton}.
 * </p>
 *
 * <p>
 * The required services are obtained from the Spring context via {@link SpringContextHolder}.
 * </p>
 */
public class AddWycieczkiButton extends Button {

    private AbstractServices<TypyWycieczek, String> typyWycieczekService;
    private WycieczkiService wycieczkiService;
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Constructs a new {@code AddWycieczkiButton} with the specified button text and associated left sidebar button.
     *
     * @param name       the text to display on the button
     * @param leftButton the left sidebar button that provides context and services for trip operations
     */
    public AddWycieczkiButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.leftButton = leftButton;
        this.setOnAction(e -> onClick());
    }

    /**
     * Handles the click event for this button.
     *
     * <p>
     * When the button is clicked, a dialog is displayed for adding a new trip. The dialog collects:
     * </p>
     * <ul>
     *   <li>A trip name from a text field.</li>
     *   <li>A trip type selected from a combo box populated with available {@link TypyWycieczek} entries.</li>
     *   <li>A start date using a {@link DatePicker}.</li>
     * </ul>
     *
     * <p>
     * The end date is automatically computed by adding the number of nights (from the selected trip type)
     * to the start date. If any field is missing, an error alert is displayed. Otherwise, a new {@link Wycieczki}
     * object is created and returned as the result of the dialog.
     * </p>
     *
     * <p>
     * Upon successful dialog completion, the new trip is added via {@link WycieczkiService} and a success alert is shown.
     * The left sidebar is then refreshed by invoking {@code onClick()} on the associated {@link CustomLeftButton}.
     * </p>
     */
    private void onClick() {
        // Retrieve the MainContent instance from the Spring context.
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Create the dialog for adding a new trip.
        Dialog<Wycieczki> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nową Wycieczkę");
        dialog.setHeaderText("Wprowadź dane nowej wycieczki.");

        // Set up dialog buttons.
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create a grid to hold form fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Create form fields.
        TextField wycieczkaField = new TextField();
        wycieczkaField.setPromptText("Nazwa Wycieczki");

        ComboBox<TypyWycieczek> typWycieczkiComboBox = new ComboBox<>();
        typWycieczkiComboBox.setItems(FXCollections.observableArrayList(typyWycieczekService.getAll()));
        typWycieczkiComboBox.setPromptText("Wybierz Typ Wycieczki");

        DatePicker poczatekDatePicker = new DatePicker();
        poczatekDatePicker.setPromptText("Data Początku");

        // Add labels and fields to the grid.
        grid.add(new Label("Nazwa Wycieczki:"), 0, 0);
        grid.add(wycieczkaField, 1, 0);
        grid.add(new Label("Typ Wycieczki:"), 0, 1);
        grid.add(typWycieczkiComboBox, 1, 1);
        grid.add(new Label("Data Początku:"), 0, 2);
        grid.add(poczatekDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Set initial focus on the trip name field.
        Platform.runLater(() -> wycieczkaField.requestFocus());

        // Convert the dialog result into a Wycieczki object.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String wycieczkaName = wycieczkaField.getText().trim();
                TypyWycieczek selectedTyp = typWycieczkiComboBox.getValue();
                LocalDate poczatek = poczatekDatePicker.getValue();

                // Automatically calculate the end date based on the trip type's duration.
                LocalDate koniec = poczatekDatePicker.getValue().plusDays(typWycieczkiComboBox.getValue().getLiczba_nocy());

                // Validate that all fields are filled.
                if (wycieczkaName.isEmpty() || selectedTyp == null || poczatek == null || koniec == null) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return null;
                }

                try {
                    Wycieczki wycieczka = new Wycieczki();
                    wycieczka.setWycieczka(wycieczkaName);
                    wycieczka.setTypWycieczki(selectedTyp);
                    wycieczka.setPoczatek(poczatek);
                    wycieczka.setKoniec(koniec);
                    wycieczka.setIlUczestinkow(0);
                    wycieczka.setWplyw(BigDecimal.valueOf(0));
                    return wycieczka;
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Liczba Uczestników musi być liczbą całkowitą, a Wpływ liczbą dziesiętną.");
                    return null;
                }
            }
            return null;
        });

        // Process the result of the dialog.
        dialog.showAndWait().ifPresent(wycieczka -> {
            try {
                wycieczkiService.add(wycieczka);
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Wycieczka została dodana pomyślnie.");
                leftButton.onClick();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas zapisywania wycieczki: " + e.getMessage());
            }
        });
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * <p>
     * This method ensures that the alert is displayed on the JavaFX Application Thread using
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
