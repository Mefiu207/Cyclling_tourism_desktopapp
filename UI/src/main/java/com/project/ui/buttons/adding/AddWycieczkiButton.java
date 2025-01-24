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

public class AddWycieczkiButton extends Button {

    private AbstractServices<TypyWycieczek, String> typyWycieczekService;
    private WycieczkiService wycieczkiService;
    private final CustomLeftButton<?, ?> leftButton;

    /**
     * Konstruktor klasy AddWycieczkiButton
     *
     * @param name Nazwa przycisku
     */
    public AddWycieczkiButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.leftButton = leftButton;

        this.setOnAction(e -> onClick());
    }

    /**
     * Metoda obsługująca kliknięcie przycisku.
     * Otwiera dialog do dodania nowej wycieczki.
     */
    private void onClick() {
        // Pobranie instancji MainContent z kontekstu Spring
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Tworzenie dialogu
        Dialog<Wycieczki> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nową Wycieczkę");
        dialog.setHeaderText("Wprowadź dane nowej wycieczki.");

        // Ustawienie przycisków
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Tworzenie formularza
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Pola formularza
        TextField wycieczkaField = new TextField();
        wycieczkaField.setPromptText("Nazwa Wycieczki");

        ComboBox<TypyWycieczek> typWycieczkiComboBox = new ComboBox<>();
        typWycieczkiComboBox.setItems(FXCollections.observableArrayList(typyWycieczekService.getAll()));
        typWycieczkiComboBox.setPromptText("Wybierz Typ Wycieczki");

        DatePicker poczatekDatePicker = new DatePicker();
        poczatekDatePicker.setPromptText("Data Początku");


        // Dodanie pól do gridu
        grid.add(new Label("Nazwa Wycieczki:"), 0, 0);
        grid.add(wycieczkaField, 1, 0);
        grid.add(new Label("Typ Wycieczki:"), 0, 1);
        grid.add(typWycieczkiComboBox, 1, 1);
        grid.add(new Label("Data Początku:"), 0, 2);
        grid.add(poczatekDatePicker, 1, 2);


        dialog.getDialogPane().setContent(grid);

        // Ustawienie fokusu na pierwszym polu
        Platform.runLater(() -> wycieczkaField.requestFocus());

        // Konwersja wyniku dialogu na obiekt Wycieczki
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String wycieczkaName = wycieczkaField.getText().trim();
                TypyWycieczek selectedTyp = typWycieczkiComboBox.getValue();
                LocalDate poczatek = poczatekDatePicker.getValue();

                // Automatycznie ustawiamy datę końcową na podstawie długości danego typu wycieczki
                LocalDate koniec = poczatekDatePicker.getValue().plusDays(typWycieczkiComboBox.getValue().getLiczba_nocy());

                // Walidacja pól
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

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(wycieczka -> {
            try {
                // Zapisanie wycieczki w bazie danych
                wycieczkiService.add(wycieczka);
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Wycieczka została dodana pomyślnie.");

                leftButton.onClick();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas zapisywania wycieczki: " + e.getMessage());
            }
        });
    }

    /**
     * Metoda pomocnicza do wyświetlania alertów.
     *
     * @param alertType Typ alertu
     * @param title     Tytuł okna
     * @param message   Wiadomość
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
