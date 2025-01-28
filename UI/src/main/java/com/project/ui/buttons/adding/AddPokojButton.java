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


public class AddPokojButton extends Button {

    private final PokojeService pokojeService;
    private final WycieczkiService wycieczkiService;
    private final KlienciService klienciServices;
    private final CustomLeftButton<?, ?> leftButton;


    public AddPokojButton(String name, CustomLeftButton<? ,?> leftButton){
        super(name);

        this.leftButton = leftButton;
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.klienciServices = SpringContextHolder.getContext().getBean(KlienciService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);

        this.setOnAction(e -> onClick());
    }

    public void onClick(){
        // Pobranie instancji MainContent z kontekstu Spring
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Tworzenie dialogu
        Dialog<Pokoje> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nowy Pokój");
        dialog.setHeaderText("Wprowadź dane nowego pokoju.");

        // Ustawienie przycisków
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Tworzenie formularza
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Pole wyboru Wycieczki z autouzupełnianiem
        Label wycieczkaLabel = new Label("Wycieczka:");
        ComboBox<Wycieczki> wycieczkiComboBox = new ComboBox<>();
        wycieczkiComboBox.setEditable(true);
        List<Wycieczki> wycieczkiList = wycieczkiService.getAll();
        ObservableList<Wycieczki> observableWycieczki = FXCollections.observableArrayList(wycieczkiList);
        wycieczkiComboBox.setItems(observableWycieczki);
        wycieczkiComboBox.setPromptText("Wybierz lub wpisz wycieczkę");

        // Ustawienie konwertera, aby ComboBox mógł wyświetlać nazwy wycieczek
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

        // Implementacja autouzupełniania
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

        // Pole tekstowe dla typu pokoju
        Label typPokojuLabel = new Label("Typ Pokoju:");
        TextField typPokojuField = new TextField();
        typPokojuField.setPromptText("Typ Pokoju");

        // Dodanie pól do gridu
        grid.add(wycieczkaLabel, 0, 0);
        grid.add(wycieczkiComboBox, 1, 0);
        grid.add(typPokojuLabel, 0, 1);
        grid.add(typPokojuField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Ustawienie fokusu na pierwszym polu
        Platform.runLater(wycieczkiComboBox::requestFocus);

        // Konwersja wyniku dialogu na obiekt Pokoje
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Wycieczki selectedWycieczka = wycieczkiComboBox.getValue();
                String typPokoju = typPokojuField.getText().trim();

                // Walidacja pól
                if (selectedWycieczka == null || typPokoju.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return null;
                }

                // Tworzenie obiektu Pokoje
                Pokoje pokoj = new Pokoje();
                pokoj.setWycieczka(selectedWycieczka);
                pokoj.setTypPokoju(typPokoju);
                pokoj.setListaHoteli(false);


                // Sprawdzamy rozmiar pokoju w zależności od nazwy
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

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(pokoj -> {
            try {
                // Zapisanie pokoju w bazie danych
                pokojeService.add(pokoj);
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Pokój został dodany pomyślnie.");

                leftButton.onClick();
            } catch (WrongLetterException e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", e.getMessage());
            } catch(Exception e){
                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas zapisywania pokoju: " + e.getMessage());
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
