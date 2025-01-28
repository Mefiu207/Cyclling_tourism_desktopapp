package com.project.ui.buttons;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.services.ListyHoteliService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.springbootjavafx.services.WycieczkiService;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.*;
import java.util.stream.Collectors;

public class RightDeleteListaHoteliButton extends Button {

    private final ListyHoteliService listyHoteliService;
    private final WycieczkiService wycieczkiService;
    private final PokojeService pokojeService;
    private final CustomLeftButton<?, ?> leftButton;


    public RightDeleteListaHoteliButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.listyHoteliService = SpringContextHolder.getContext().getBean(ListyHoteliService.class);
        this.wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);

        this.leftButton = leftButton;

        this.setOnAction(e -> onClick());
    }

    public void  onClick(){

        Wycieczki wycieczka = wybierzWycieczke();
        List<Pokoje> pokoje = wybierzPokoje(wycieczka);

        if(pokoje == null) return;

        pokoje.forEach(listyHoteliService::usunDlaPokoju);

        pokojeService.setListaHoteliFalse(pokoje);

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Usunięto listy hoteli");
        leftButton.onClick();
    }


    private List<Pokoje> wybierzPokoje(Wycieczki wycieczka) {

        Dialog<List<Pokoje>> dialog = new Dialog<>();
        dialog.setTitle("Wybierz pokoje");
        dialog.setHeaderText("Wybierz pokoje dla których chcesz usunąć listę hoteli");

        ButtonType nextRoomButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nextRoomButtonType, ButtonType.CANCEL);

        List<Pokoje> dostepnePokoje = pokojeService.getPokojeZListami(wycieczka);

        if (dostepnePokoje.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak Dostępnych Pokoi",
                    "Dla wybranej wycieczki nie ma dostępnych pokoi z listami hoteli.");
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
                List<Pokoje> selectedPokoje = wybranePokoje.stream().toList();
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
        dialog.setHeaderText("Wybierz wycieczkę dla której chcesz usunąć listy hoteli");

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
