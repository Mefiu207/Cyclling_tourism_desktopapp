package com.project.ui.buttons;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.ListaNocyHoteli;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.ListaNocyHoteliService;
import com.project.springbootjavafx.services.PokojeService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.*;

public class ListaHoteliPokaż extends Button {


    private final PokojeService pokojeService;
    private final ListaNocyHoteliService listaNocyHoteliService;
    private final MainContent mainContent;


    public ListaHoteliPokaż(String name){
        super(name);

        this.pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.mainContent = SpringContextHolder.getContext().getBean(MainContent.class);
        this.listaNocyHoteliService = SpringContextHolder.getContext().getBean(ListaNocyHoteliService.class);


        this.setOnAction(e -> onClick());
    }

    public void onClick() {
        Pokoje pokoj = wybierzPokoj();
        if (pokoj == null) return;

        // Pobranie danych widoku ListaNocyHoteli dla wybranego pokoju
        List<ListaNocyHoteli> listaNocyHoteli = listaNocyHoteliService.getListyByPokoj(pokoj);

        if (listaNocyHoteli.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Brak danych", "Dla wybranego pokoju nie ma danych do wyświetlenia.");
            return;
        }

        // Tworzenie TableView
        TableView<ListaNocyHoteli> tableView = new TableView<>();

        // Kolumna: noc
        TableColumn<ListaNocyHoteli, Integer> columnNoc = new TableColumn<>("Noc");
        columnNoc.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNoc()));

        // Kolumna: data
        TableColumn<ListaNocyHoteli, String> columnData = new TableColumn<>("Data");
        columnData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getData().toString()
        ));

        // Kolumna: miasto
        TableColumn<ListaNocyHoteli, String> columnMiasto = new TableColumn<>("Miasto");
        columnMiasto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMiasto()));

        // Kolumna: hotel
        TableColumn<ListaNocyHoteli, String> columnHotel = new TableColumn<>("Hotel");
        columnHotel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHotel()));

        // Dodawanie kolumn do TableView
        tableView.getColumns().addAll(columnNoc, columnData, columnMiasto, columnHotel);

        // Ustawienie danych w TableView
        tableView.getItems().addAll(listaNocyHoteli);

        // Aktualizacja widoku głównego
        mainContent.updateContent(tableView);
    }

    private Pokoje wybierzPokoj(){

        Dialog<Pokoje> dialog = new Dialog<>();

        dialog.setTitle("Wybierz pokój");
        dialog.setHeaderText("Podaj Id pokoju którego listę hoteli chcesz wyświetlić");

        ButtonType addButtonType = new ButtonType("Dalej", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField pokojID = new TextField();
        pokojID.setPromptText("ID pokoju");


        grid.add(new Label("ID pokoju:"), 0, 1);
        grid.add(pokojID, 1, 1);


        dialog.getDialogPane().setContent(grid);

        Platform.runLater(pokojID::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String ID = pokojID.getText().trim();

                try {
                    return pokojeService.getById(Integer.valueOf(ID));
                } catch (NoSuchElementException e) {
                    showAlert(Alert.AlertType.ERROR, "Brak obiektu", "Nie ma pokoju o takim ID");
                }
            }
            return null;
        });

        Optional<Pokoje> result = dialog.showAndWait();
        if(result.isPresent()) {

            if (!result.get().getListaHoteli()) {
                showAlert(Alert.AlertType.ERROR, "Brak listy hoteli", "Najpierw wybierz hotele dla pokoju");
                return null;
            }

            if(result.get().getIlKlientow() < result.get().getIlMiejsc()){
                showAlert(Alert.AlertType.INFORMATION, "Uwaga", "Na ten moment ten pokój nie jest zapełniony");
            }
        }



        return result.orElse(null);
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
