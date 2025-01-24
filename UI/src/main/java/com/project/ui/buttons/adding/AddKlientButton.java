package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.WrongLetterException;

import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;

import com.project.springbootjavafx.services.KlienciService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.services.WycieczkiService;

import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddKlientButton extends Button {

    private final WycieczkiService wycieczkiService;
    private final PokojeService pokojeService;
    private final KlienciService klienciService;
    private final CustomLeftButton<?, ?> leftButton;


    
    public AddKlientButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.leftButton = leftButton;
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.klienciService = SpringContextHolder.getContext().getBean(KlienciService.class);

        this.setOnAction(e -> onClick());
    }

    public void onClick() {


        // Tworzenie pierwszego dialogu do wprowadzenia danych klienta
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Nowego Klienta");
        dialog.setHeaderText("Wprowadź dane nowego klienta.");

        // Ustawienie przycisków
        ButtonType nextButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

        // Tworzenie formularza
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Pola formularza
        TextField imieField = new TextField();
        imieField.setPromptText("Imię");

        TextField nazwiskoField = new TextField();
        nazwiskoField.setPromptText("Nazwisko");

        // Pole wyboru wycieczki z autouzupełnianiem
        ComboBox<Wycieczki> wycieczkiComboBox = new ComboBox<>();
        wycieczkiComboBox.setEditable(true);
        List<Wycieczki> wycieczkiList = wycieczkiService.getAll();
        ObservableList<Wycieczki> observableWycieczki = FXCollections.observableArrayList(wycieczkiList);
        wycieczkiComboBox.setItems(observableWycieczki);
        wycieczkiComboBox.setPromptText("Wybierz lub wpisz wycieczkę");

        // Ustawienie konwertera, aby ComboBox poprawnie wyświetlał nazwy wycieczek
        wycieczkiComboBox.setConverter(new StringConverter<Wycieczki>() {
            @Override
            public String toString(Wycieczki wycieczki) {
                return wycieczki != null ? wycieczki.toString() : "";
            }

            @Override
            public Wycieczki fromString(String string) {
                return wycieczkiList.stream()
                        .filter(wycieczka -> wycieczka.toString().equalsIgnoreCase(string))
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

        // Dodanie pól do gridu
        grid.add(new Label("Imię:"), 0, 0);
        grid.add(imieField, 1, 0);
        grid.add(new Label("Nazwisko:"), 0, 1);
        grid.add(nazwiskoField, 1, 1);
        grid.add(new Label("Wycieczka:"), 0, 2);
        grid.add(wycieczkiComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Ustawienie fokusu na pierwszym polu
        Platform.runLater(imieField::requestFocus);

        // Obsługa przycisku "Dalej"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == nextButtonType) {
                String imie = imieField.getText().trim();
                String nazwisko = nazwiskoField.getText().trim();
                Wycieczki wycieczka = wycieczkiComboBox.getValue();

                // Walidacja pól
                if (imie.isEmpty() || nazwisko.isEmpty() || wycieczka == null) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wszystkie pola muszą być wypełnione.");
                    return false;
                }

                return true; // Return null because we handle dialogs sequentially
            }
            return false;
        });

        // Wyświetlenie pierwszego dialogu i obsługa wyniku
        Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
        // Przechowywanie danych tymczasowych
            String tempImie = imieField.getText().trim();
            String tempNazwisko = nazwiskoField.getText().trim();
            Wycieczki tempWycieczka = wycieczkiComboBox.getValue();

            // Tworzenie drugiego dialogu do wyboru pokoju
            Dialog<Void> roomDialog = new Dialog<>();
            roomDialog.setTitle("Wybierz Pokój");
            roomDialog.setHeaderText("Wybierz pokój dla klienta.");

            // Ustawienie przycisków
            ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
            roomDialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

            // Pobranie dostępnych pokoi dla wybranej wycieczki
            List<Pokoje> dostepnePokoje = pokojeService.getAll().stream()
                    .filter(pokoj -> pokoj.getWycieczka().equals(tempWycieczka))
                    .filter(pokoj -> pokoj.getIlKlientow() < pokoj.getIlMiejsc())
                    .collect(Collectors.toList());


            if (dostepnePokoje.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                        "Dla wybranej wycieczki nie ma dostępnych pokoi.");
                return;
            }

            // Tworzenie ListView do wyboru pokoju
            ListView<Pokoje> pokojeListView = new ListView<>();
            ObservableList<Pokoje> observablePokoje = FXCollections.observableArrayList(dostepnePokoje);
            pokojeListView.setItems(observablePokoje);
            pokojeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Tylko jeden wybór
            pokojeListView.setPrefHeight(150);

            // Ustawienie konwertera, aby ListView poprawnie wyświetlał pokój
            pokojeListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Pokoje pokoj, boolean empty) {
                    super.updateItem(pokoj, empty);
                    if (empty || pokoj == null) {
                        setText(null);
                    } else {
                        setText("Typ: " + pokoj.getTypPokoju());
                    }
                }
            });

            VBox roomContent = new VBox();
            roomContent.setSpacing(10);
            roomContent.getChildren().addAll(new Label("Wybierz pokój:"), pokojeListView);

            roomDialog.getDialogPane().setContent(roomContent);

            // Ustawienie fokusu na ListView
            Platform.runLater(pokojeListView::requestFocus);

            // Obsługa przycisku "Dalej" w drugim dialogu
            roomDialog.setResultConverter(dialogButtonRoom -> {
                if (dialogButtonRoom == nextRoomButtonType) {
                    Pokoje selectedPokoj = pokojeListView.getSelectionModel().getSelectedItem();
                    if (selectedPokoj == null) {
                        showAlert(Alert.AlertType.ERROR, "Błąd", "Musisz wybrać pokój.");
                        return null;
                    }

                    // Tworzenie trzeciego dialogu do wyboru dodatkowych usług
                    Dialog<Void> servicesDialog = new Dialog<>();
                    servicesDialog.setTitle("Dodatkowe Usługi");
                    servicesDialog.setHeaderText("Wybierz, z których usług korzysta klient.");

                    // Ustawienie przycisków
                    ButtonType addServicesButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
                    servicesDialog.getDialogPane().getButtonTypes().addAll(addServicesButtonType, ButtonType.CANCEL);

                    // Tworzenie checkboxów
                    CheckBox ulgaCheckBox = new CheckBox("Ulga");
                    CheckBox rowerCheckBox = new CheckBox("Rower");
                    CheckBox eBikeCheckBox = new CheckBox("E-Bike");
                    CheckBox noclegPrzedCheckBox = new CheckBox("Nocleg Przed");
                    CheckBox noclegPoCheckBox = new CheckBox("Nocleg Po");
                    CheckBox hbCheckBox = new CheckBox("HB");

                    VBox servicesContent = new VBox(10);
                    servicesContent.getChildren().addAll(
                            ulgaCheckBox, rowerCheckBox, eBikeCheckBox,
                            noclegPrzedCheckBox, noclegPoCheckBox, hbCheckBox
                    );

                    servicesDialog.getDialogPane().setContent(servicesContent);

                    // Ustawienie fokusu na pierwszym checkboxie
                    Platform.runLater(() -> ulgaCheckBox.requestFocus());

                    // Obsługa przycisku "Dodaj" w trzecim dialogu
                    servicesDialog.setResultConverter(dialogButtonServices -> {
                        if (dialogButtonServices == addServicesButtonType) {
                            boolean ulga = ulgaCheckBox.isSelected();
                            boolean rower = rowerCheckBox.isSelected();
                            boolean eBike = eBikeCheckBox.isSelected();
                            boolean noclegPrzed = noclegPrzedCheckBox.isSelected();
                            boolean noclegPo = noclegPoCheckBox.isSelected();
                            boolean hb = hbCheckBox.isSelected();

                            // Tworzenie obiektu Klienci
                            Klienci klient = new Klienci();
                            klient.setImie(tempImie);
                            klient.setNazwisko(tempNazwisko);
                            klient.setWycieczka(tempWycieczka);
                            klient.setPokoj(selectedPokoj);
                            klient.setUlga(ulga);
                            klient.setRower(rower);
                            klient.setEBike(eBike);
                            klient.setNoclegPrzed(noclegPrzed);
                            klient.setNoclegPo(noclegPo);
                            klient.setHb(hb);
                            klient.setTypPokoju(selectedPokoj.getTypPokoju());
                            klient.setDoZaplaty(BigDecimal.valueOf(0));


                            // Ceny wycieczki na której jest klient
                            Ceny ceny = klient.getWycieczka().getTypWycieczki().getCeny();

                            switch(selectedPokoj.getIlMiejsc()){
                                case 1:
                                    klient.setDoZaplaty(ceny.getPok_1().add(klient.getDoZaplaty()));
                                    break;
                                case 2:
                                    klient.setDoZaplaty(ceny.getPok_2().add(klient.getDoZaplaty()));
                                    break;
                                case 3:
                                    klient.setDoZaplaty(ceny.getPok_3().add(klient.getDoZaplaty()));
                                    break;
                                case 4:
                                    klient.setDoZaplaty(ceny.getPok_4().add(klient.getDoZaplaty()));
                                    break;
                            }

                            if(klient.getUlga()){
                                Double val = klient.getDoZaplaty().intValue() * (ceny.getUlga_dziecko() - 100)/100.0;
                                klient.setDoZaplaty(BigDecimal.valueOf(val));
                            }

                            if(klient.getRower()){
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getRower()));
                            }

                            if(klient.getEBike()){
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getE_bike()));
                            }

                            if(klient.getNoclegPo()){
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getDodatkowa_noc()));
                            }

                            if(klient.getNoclegPrzed()){
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getDodatkowa_noc()));
                            }

                            if(klient.getHb()){
                                klient.setDoZaplaty(klient.getDoZaplaty().add(ceny.getHb()));
                            }

                            // Zapisanie klienta w bazie danych
                            try {
                                klienciService.add(klient);
                                leftButton.onClick();

                                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Klient został dodany pomyślnie.");



                            } catch (WrongLetterException ex) {
                                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem: " + ex.getMessage());
                            } catch (Exception ex) {
                                showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił nieoczekiwany problem: " + ex.getMessage());
                            }

                            return null;
                        }
                        return null;
                    });

                    // Wyświetlenie trzeciego dialogu
                    servicesDialog.showAndWait();
                    return null;
                }
                return null;
            });

            // Wyświetlenie drugiego dialogu
            roomDialog.showAndWait();
        }
    }


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