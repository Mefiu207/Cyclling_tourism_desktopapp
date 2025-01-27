package com.project.ui.buttons.adding;

import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.*;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;


import java.util.*;
import java.util.stream.Collectors;


public class AddListaHoteliButton extends Button {

    private final ListyHoteliService listyHoteliService;
    private final WycieczkiService wycieczkiService;
    private final PokojeService pokojeService;
    private final TypyWycieczekService typyWycieczekService;
    private final HoteleService hoteleService;


    public AddListaHoteliButton(String name) {
        super(name);

        this.listyHoteliService = SpringContextHolder.getContext().getBean(ListyHoteliService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        this.hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);

        this.setOnAction(e -> onClick());
    }

    public void onClick(){

        Wycieczki wycieczka = wybierzWycieczke();
        List<Pokoje> wybranePokoje = wybierzPokoje(wycieczka);

        if(wybranePokoje == null) return;

        List<ListyHoteli> listaHoteli = wybierzHotele(wybranePokoje);

        if(listaHoteli == null) return;

        for(ListyHoteli lista : listaHoteli){



            listyHoteliService.add(lista);
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dodano listy hoteli");
    }

    private List<ListyHoteli> wybierzHotele(List<Pokoje> wybranePokoje) {

        Dialog<List<ListyHoteli>> dialog = new Dialog<>();
        dialog.setTitle("Listy hoteli");
        dialog.setHeaderText("Ustaw hotele dla nocy wycieczki");

        ButtonType confirmButton = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Typ wycieczki i maista danej wycieczki
        TypyWycieczek typWycieczki = wybranePokoje.get(0).getWycieczka().getTypWycieczki();
        List<MiastaWycieczek> miastaWycieczki = typyWycieczekService.getMiastaWycieczki(typWycieczki);

        // Tu przechowujemy wybrane hotele
        List<Hotele> hoteleWycieczki = new ArrayList<>(Collections.nCopies(miastaWycieczki.size(), null));

        // Iteracja po ilosci nocy danej wycieczki i dodawanie hoteli + autouzupelnienei
        for (int i = 0; i < miastaWycieczki.size(); i++) {

            Label miastoLabel = new Label(miastaWycieczki.get(i).getMiasta().getMiasto());
            TextField hotelField = new TextField();
            hotelField.setPromptText("Kod hotelu");

            List<Hotele> hoteleMiasta = hoteleService.getHoteleMiasta(miastaWycieczki.get(i).getMiasta());
            ObservableList<Hotele> observableHotele = FXCollections.observableArrayList(hoteleMiasta);

            ComboBox<Hotele> hotelCombo = new ComboBox<>(observableHotele);
            hotelCombo.setItems(observableHotele);
            hotelCombo.setPromptText("Wybierz lub wpisz hotel");
            hotelCombo.setEditable(true);


            hotelCombo.setConverter(new StringConverter<Hotele>() {
                @Override
                public String toString(Hotele hotele) {
                    return hotele == null ? "" : hotele.getKod();
                }

                @Override
                public Hotele fromString(String s) {
                    return hoteleMiasta.stream()
                            .filter(hotel -> hotel.getKod().equalsIgnoreCase(s))
                            .findFirst()
                            .orElse(null);
                }
            });

            hotelCombo.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.isEmpty()) {
                    hotelCombo.hide();
                    hotelCombo.setItems(observableHotele);
                } else {
                    List<Hotele> filtered = hoteleMiasta.stream()
                            .filter(hotel -> hotel.toString().toLowerCase().contains(newVal.toLowerCase()))
                            .collect(Collectors.toList());
                    hotelCombo.setItems(FXCollections.observableArrayList(filtered));
                    hotelCombo.show();
                }
            });

            final int index = i;
            hotelCombo.valueProperty().addListener((a, old, New) -> {
                hoteleWycieczki.set(index, New);
            });


            grid.add(miastoLabel, 0, i);
            grid.add(hotelCombo, 1, i);
        }

        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(buttonType -> {
            if(buttonType == confirmButton) {
                List<ListyHoteli> listyDoZwrotu = new ArrayList<>();

                // Dla każdego pokoju tworzymy listy wybranych hoteli
                for (Pokoje pokoje : wybranePokoje) {

                    // Dla każdego pokoju każde miasto
                    for (int j = 0; j < hoteleWycieczki.size(); j++) {
                        ListyHoteli nowaLista = new ListyHoteli();

                        nowaLista.setPokoj(pokoje);
                        nowaLista.setMiastoWycieczki(miastaWycieczki.get(j));
                        nowaLista.setHotel(hoteleWycieczki.get(j));

                        listyDoZwrotu.add(nowaLista);
                    }
                }
                return listyDoZwrotu;
            }
            return null;
        });


        Optional<List<ListyHoteli>> optionalResult = dialog.showAndWait();

        return optionalResult.orElse(null);
    }


    private List<Pokoje> wybierzPokoje(Wycieczki wycieczka) {

        Dialog<List<Pokoje>> dialog = new Dialog<>();
        dialog.setTitle("Wybierz pokoje");
        dialog.setHeaderText("Wybierz pokoje dla których chcesz stworzyć listę hoteli");

        ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

        List<Pokoje> dostepnePokoje = pokojeService.getPokojeWycieczki(wycieczka);

        if (dostepnePokoje.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                    "Dla wybranej wycieczki nie ma dostępnych pokoi.");
            return null;
        }


        ListView<Pokoje> pokojeListView = new ListView<>();
        ObservableList<Pokoje> observablePokoje = FXCollections.observableArrayList(dostepnePokoje);
        pokojeListView.setItems(observablePokoje);
        pokojeListView.setPrefHeight(150);

        Set<Pokoje> wybranePokoje = new HashSet<>();

        pokojeListView.setCellFactory(param -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Pokoje pokoj, boolean empty) {
                    super.updateItem(pokoj, empty);
                    if (empty || pokoj == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText("Pokój ID: " + pokoj.getId() + ", Typ: " + pokoj.getTypPokoju());
                        // Podświetl, jeśli w zbiorze wybranePokoje
                        if (wybranePokoje.contains(pokoj)) {
                            setStyle("-fx-background-color: #5db20e;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });

        pokojeListView.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode(); // węzeł, w który kliknięto
            while (node != null && !(node instanceof ListCell)) {
                node = node.getParent(); // idź w górę hierarchii do momentu trafienia na ListCell
            }
            if (node instanceof ListCell cell && !cell.isEmpty()) {
                Pokoje pokoj = (Pokoje) cell.getItem();
                if (wybranePokoje.contains(pokoj)) {
                    wybranePokoje.remove(pokoj);
                } else {
                    wybranePokoje.add(pokoj);
                }
                pokojeListView.refresh();
            }
        });


        VBox roomContent = new VBox();
        roomContent.setSpacing(10);
        roomContent.getChildren().addAll(new Label("Wybierz pokoje:"), pokojeListView);


        dialog.getDialogPane().setContent(roomContent);
        Platform.runLater(pokojeListView::requestFocus);


        dialog.setResultConverter(dialogButtonRoom -> {
            if (dialogButtonRoom == nextRoomButtonType) {
                ObservableList<Pokoje> selectedPokoje = pokojeListView.getSelectionModel().getSelectedItems();
                if (selectedPokoje == null || selectedPokoje.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Musisz wybrać przynajmniej jeden pokój.");
                    return null;
                }
                return new ArrayList<>(selectedPokoje);
            }
            return null;
        });

        Optional<List<Pokoje>> result = dialog.showAndWait();

        return result.orElse(null);
    }


    private Wycieczki wybierzWycieczke(){

        Dialog<Wycieczki> dialog = new Dialog<>();
        dialog.setTitle("Wybór wycieczki");
        dialog.setHeaderText("Wybierz wycieczkę dla której chcesz stworzyć listę hoteli");

        ButtonType nextButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

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

        grid.add(new Label("Wycieczka:" ), 0, 0);
        grid.add(wycieczkiComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(wycieczkiComboBox::requestFocus);

        dialog.showAndWait();
        return wycieczkiComboBox.getValue();
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
