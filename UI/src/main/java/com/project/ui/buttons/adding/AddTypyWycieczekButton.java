package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.TypyWycieczek;

import com.project.springbootjavafx.services.CenyService;
import com.project.springbootjavafx.services.TypyWycieczekService;

import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class AddTypyWycieczekButton extends Button {

    private final CustomLeftButton<?, ?> leftButton;
    private final TypyWycieczekService typyService;
    private final CenyService cenyService;

    public AddTypyWycieczekButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.leftButton = leftButton;
        this.typyService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.cenyService = SpringContextHolder.getContext().getBean(CenyService.class);

        this.setOnAction(e -> openAddTypyDialog());
    }

    public void openAddTypyDialog() {
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Typ Wycieczki");
        dialog.setHeaderText("Wprowadź informacje o nowym typie wycieczki");

        // Ustawienie przycisków
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Tworzenie siatki do wprowadzenia danych
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Pola tekstowe do wprowadzenia danych
        TextField typField = new TextField();
        typField.setPromptText("Typ Wycieczki");

        TextField liczbaNocyField = new TextField();
        liczbaNocyField.setPromptText("Liczba Nocy");

        grid.add(new Label("Typ Wycieczki:"), 0, 0);
        grid.add(typField, 1, 0);
        grid.add(new Label("Liczba Nocy:"), 0, 1);
        grid.add(liczbaNocyField, 1, 1);

        Platform.runLater(typField::requestFocus);
        dialog.getDialogPane().setContent(grid);

        // Konwersja wyniku dialogu na obiekt TypyWycieczek
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String typ = typField.getText().trim();
                String liczbaNocyStr = liczbaNocyField.getText().trim();
                if (typ.isEmpty() || liczbaNocyStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return null;
                }
                try {
                    Integer liczbaNocy = Integer.parseInt(liczbaNocyStr);
                    TypyWycieczek typWycieczki = new TypyWycieczek();
                    typWycieczki.setTyp(typ);
                    typWycieczki.setLiczba_nocy(liczbaNocy);
                    return typWycieczki;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Liczba nocy musi być liczbą.");
                    return null;
                }
            }
            return null;
        });

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(typWycieczki -> {
            try {
                typyService.add(typWycieczki); // Zapisanie nowego typu wycieczki
                leftButton.onClick(); // Odświeżenie tabeli
                openAddCenyDialog(typWycieczki); // Otwarcie dialogu do dodania cen
            } catch (DuplicatedEntityExceptionn e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", e.getMessage());
            }
        });
    }

    /**
     * Metoda otwierająca dialog do dodawania cen dla danego typu wycieczki.
     */
    private void openAddCenyDialog(TypyWycieczek typWycieczki) {
        Dialog<Ceny> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Ceny dla Typu Wycieczki: " + typWycieczki.getTyp());
        dialog.setHeaderText("Wprowadź ceny dla nowego typu wycieczki");

        // Ustawienie przycisków
        ButtonType addButtonType = new ButtonType("Dodaj Ceny", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Tworzenie siatki do wprowadzenia danych
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Pola tekstowe do wprowadzenia cen
        TextField pok1Field = new TextField();
        pok1Field.setPromptText("Pokój 1");

        TextField pok2Field = new TextField();
        pok2Field.setPromptText("Pokój 2");

        TextField pok3Field = new TextField();
        pok3Field.setPromptText("Pokój 3");

        TextField pok4Field = new TextField();
        pok4Field.setPromptText("Pokój 4");

        TextField ulgaDzieckoField = new TextField();
        ulgaDzieckoField.setPromptText("Ulga Dziecko (%)");

        TextField rowerField = new TextField();
        rowerField.setPromptText("Rower");

        TextField eBikeField = new TextField();
        eBikeField.setPromptText("E-Bike");

        TextField dodatkowaNocField = new TextField();
        dodatkowaNocField.setPromptText("Dodatkowa Noc");

        TextField hbField = new TextField();
        hbField.setPromptText("HB");

        // Dodanie pól do siatki
        grid.add(new Label("Pokój 1:"), 0, 0);
        grid.add(pok1Field, 1, 0);
        grid.add(new Label("Pokój 2:"), 0, 1);
        grid.add(pok2Field, 1, 1);
        grid.add(new Label("Pokój 3:"), 0, 2);
        grid.add(pok3Field, 1, 2);
        grid.add(new Label("Pokój 4:"), 0, 3);
        grid.add(pok4Field, 1, 3);
        grid.add(new Label("Ulga Dziecko (%):"), 0, 4);
        grid.add(ulgaDzieckoField, 1, 4);
        grid.add(new Label("Rower:"), 0, 5);
        grid.add(rowerField, 1, 5);
        grid.add(new Label("E-Bike:"), 0, 6);
        grid.add(eBikeField, 1, 6);
        grid.add(new Label("Dodatkowa Noc:"), 0, 7);
        grid.add(dodatkowaNocField, 1, 7);
        grid.add(new Label("HB:"), 0, 8);
        grid.add(hbField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Ustawienie fokusu na pierwsze pole tekstowe
        Platform.runLater(pok1Field::requestFocus);

        // Konwersja wyniku dialogu na obiekt Ceny
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    BigDecimal pok1 = new BigDecimal(pok1Field.getText().trim());
                    BigDecimal pok2 = new BigDecimal(pok2Field.getText().trim());
                    BigDecimal pok3 = new BigDecimal(pok3Field.getText().trim());
                    BigDecimal pok4 = new BigDecimal(pok4Field.getText().trim());
                    Integer ulgaDziecko = Integer.parseInt(ulgaDzieckoField.getText().trim());
                    BigDecimal rower = new BigDecimal(rowerField.getText().trim());
                    BigDecimal eBike = new BigDecimal(eBikeField.getText().trim());
                    BigDecimal dodatkowaNoc = new BigDecimal(dodatkowaNocField.getText().trim());
                    BigDecimal hb = new BigDecimal(hbField.getText().trim());

                    Ceny ceny = new Ceny();
                    ceny.setTyp_wycieczki(typWycieczki);
                    ceny.setPok_1(pok1);
                    ceny.setPok_2(pok2);
                    ceny.setPok_3(pok3);
                    ceny.setPok_4(pok4);
                    ceny.setUlga_dziecko(ulgaDziecko);
                    ceny.setRower(rower);
                    ceny.setE_bike(eBike);
                    ceny.setDodatkowa_noc(dodatkowaNoc);
                    ceny.setHb(hb);

                    return ceny;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą zawierać prawidłowe wartości.");
                    return null;
                }
            }
            return null;
        });

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(ceny -> {
            try {
                cenyService.add(ceny); // Zapisanie cen
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Ceny zostały dodane.");
                leftButton.onClick(); // Odświeżenie tabeli
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się dodać cen: " + e.getMessage());
            }
        });
    }

    /**
     * Metoda pokazująca alert z określonym typem, tytułem i wiadomością.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> { // Upewnienie się, że alert jest wyświetlany na właściwym wątku
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
