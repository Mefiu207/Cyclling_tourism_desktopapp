package com.project.ui.buttons;

import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.TypyWycieczek;


import com.project.springbootjavafx.services.AbstractServices;
import com.project.springbootjavafx.services.CenyService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import java.math.BigDecimal;
import javafx.scene.control.cell.PropertyValueFactory;

public class RightCenyButton extends Button {

    private AbstractServices typyWycieczekService;
    private CenyService cenyService;


    public RightCenyButton(String name,
                           CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.typyWycieczekService = leftButton.getServices();
        this.cenyService = SpringContextHolder.getContext().getBean(CenyService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Metoda obsługująca kliknięcie przycisku.
     * Otwiera dialog do wyboru TypyWycieczek i wyświetla odpowiadające Ceny.
     */
    private void onClick() {
        // Pobranie instancji MainContent z kontekstu Spring
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Tworzenie dialogu wyboru TypyWycieczek
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Wybierz Typ Wycieczki");
        dialog.setHeaderText("Wybierz typ wycieczki, dla którego chcesz zobaczyć ceny.");

        // Ustawienie przycisków
        ButtonType viewPricesButtonType = new ButtonType("Zobacz Ceny", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(viewPricesButtonType, ButtonType.CANCEL);

        // Tworzenie ComboBox z TypyWycieczek
        ComboBox<TypyWycieczek> typyWycieczekComboBox = new ComboBox<>();
        typyWycieczekComboBox.setItems(FXCollections.observableArrayList(typyWycieczekService.getAll()));
        typyWycieczekComboBox.setPromptText("Wybierz Typ Wycieczki");

        // Dodanie ComboBox do dialogu
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(typyWycieczekComboBox);
        dialog.getDialogPane().setContent(content);

        // Ustawienie fokusu na ComboBox
        Platform.runLater(() -> typyWycieczekComboBox.requestFocus());

        // Konwersja wyniku dialogu na obiekt TypyWycieczek
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == viewPricesButtonType) {
                return typyWycieczekComboBox.getValue();
            }
            return null;
        });

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(selectedTyp -> {
            if (selectedTyp != null) {


                try {
                    Ceny cenyList = cenyService.findByTypWycieczki(selectedTyp);

                    if (cenyList == null) {
                        showAlert(Alert.AlertType.INFORMATION, "Brak Danych", "Dla wybranego typu wycieczki nie ma dostępnych cen.");
                        return;
                    }

                    // Tworzenie TableView dla Ceny
                    TableView<Ceny> cenyTableView = new TableView<>();

                    // Definiowanie kolumn
                    TableColumn<Ceny, BigDecimal> pok1Column = new TableColumn<>("Pokój 1");
                    pok1Column.setCellValueFactory(new PropertyValueFactory<>("pok_1"));

                    TableColumn<Ceny, BigDecimal> pok2Column = new TableColumn<>("Pokój 2");
                    pok2Column.setCellValueFactory(new PropertyValueFactory<>("pok_2"));

                    TableColumn<Ceny, BigDecimal> pok3Column = new TableColumn<>("Pokój 3");
                    pok3Column.setCellValueFactory(new PropertyValueFactory<>("pok_3"));

                    TableColumn<Ceny, BigDecimal> pok4Column = new TableColumn<>("Pokój 4");
                    pok4Column.setCellValueFactory(new PropertyValueFactory<>("pok_4"));

                    TableColumn<Ceny, Integer> ulgaDzieckoColumn = new TableColumn<>("Ulga Dziecko (%)");
                    ulgaDzieckoColumn.setCellValueFactory(new PropertyValueFactory<>("ulga_dziecko"));

                    TableColumn<Ceny, BigDecimal> rowerColumn = new TableColumn<>("Rower");
                    rowerColumn.setCellValueFactory(new PropertyValueFactory<>("rower"));

                    TableColumn<Ceny, BigDecimal> eBikeColumn = new TableColumn<>("E-Bike");
                    eBikeColumn.setCellValueFactory(new PropertyValueFactory<>("e_bike"));

                    TableColumn<Ceny, BigDecimal> dodatkowaNocColumn = new TableColumn<>("Dodatkowa Noc");
                    dodatkowaNocColumn.setCellValueFactory(new PropertyValueFactory<>("dodatkowa_noc"));

                    TableColumn<Ceny, BigDecimal> hbColumn = new TableColumn<>("HB");
                    hbColumn.setCellValueFactory(new PropertyValueFactory<>("hb"));

                    // Dodanie kolumn do TableView
                    cenyTableView.getColumns().addAll(
                            pok1Column, pok2Column, pok3Column, pok4Column,
                            ulgaDzieckoColumn, rowerColumn, eBikeColumn, dodatkowaNocColumn, hbColumn
                    );

                    // Ustawienie danych w TableView
                    cenyTableView.setItems(FXCollections.observableArrayList(cenyList));

                    // Opcjonalnie: Dostosowanie szerokości kolumn
                    cenyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Aktualizacja zawartości MainContent
                    mainContent.updateContent(cenyTableView);
                }
                catch(IllegalArgumentException e){
                    showAlert(Alert.AlertType.ERROR, "Typ jest null", e.getMessage());
                }

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

