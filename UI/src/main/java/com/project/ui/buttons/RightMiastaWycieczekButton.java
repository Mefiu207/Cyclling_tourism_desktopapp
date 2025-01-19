package com.project.ui.buttons;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.services.AbstractServices;
import com.project.springbootjavafx.services.MiastaWycieczekService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class RightMiastaWycieczekButton extends Button {

    private AbstractServices typyWycieczekService;
    private MiastaWycieczekService miastaWycieczekService;

    /**
     * Konstruktor klasy RightMiastaWycieczekButton
     *
     * @param name        Nazwa przycisku
     * @param leftButton  Referencja do CustomLeftButton (jeśli potrzebna)
     */
    public RightMiastaWycieczekButton(String name,
                                      CustomLeftButton<?, ?> leftButton) {
        super(name);
        this.typyWycieczekService = leftButton.getServices();
        this.miastaWycieczekService = SpringContextHolder.getContext().getBean(MiastaWycieczekService.class);
        this.setOnAction(e -> onClick());
    }

    /**
     * Metoda obsługująca kliknięcie przycisku.
     * Otwiera dialog do wyboru TypyWycieczek i wyświetla odpowiadające MiastaWycieczek.
     */
    private void onClick() {
        // Pobranie instancji MainContent z kontekstu Spring
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Tworzenie dialogu wyboru TypyWycieczek
        Dialog<TypyWycieczek> dialog = new Dialog<>();
        dialog.setTitle("Wybierz Typ Wycieczki");
        dialog.setHeaderText("Wybierz typ wycieczki, dla którego chcesz zobaczyć miasta.");

        // Ustawienie przycisków
        ButtonType viewCitiesButtonType = new ButtonType("Zobacz Miasta", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(viewCitiesButtonType, ButtonType.CANCEL);

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
            if (dialogButton == viewCitiesButtonType) {
                return typyWycieczekComboBox.getValue();
            }
            return null;
        });

        // Obsługa wyniku dialogu
        dialog.showAndWait().ifPresent(selectedTyp -> {
            if (selectedTyp != null) {
                try {
                    // Pobranie listy MiastaWycieczek dla wybranego typu wycieczki
                    List<MiastaWycieczek> miastaWycieczekList = miastaWycieczekService.findByTypWycieczki(selectedTyp);

                    if (miastaWycieczekList == null || miastaWycieczekList.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "Brak Danych", "Dla wybranego typu wycieczki nie ma przypisanych miast.");
                        return;
                    }

                    // Tworzenie TableView dla MiastaWycieczek
                    TableView<MiastaWycieczek> miastaTableView = new TableView<>();


                    TableColumn<MiastaWycieczek, String> miastoColumn = new TableColumn<>("Miasto");
                    miastoColumn.setCellValueFactory(cellData -> {
                        if (cellData.getValue().getMiasta() != null) {
                            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMiasta().getMiasto());
                        } else {
                            return new javafx.beans.property.SimpleStringProperty("Brak");
                        }
                    });

                    TableColumn<MiastaWycieczek, Integer> numerNocyColumn = new TableColumn<>("Numer Nocy");
                    numerNocyColumn.setCellValueFactory(new PropertyValueFactory<>("numerNocy"));

                    // Dodanie kolumn do TableView
                    miastaTableView.getColumns().addAll(miastoColumn, numerNocyColumn);

                    // Ustawienie danych w TableView
                    miastaTableView.setItems(FXCollections.observableArrayList(miastaWycieczekList));

                    // Opcjonalnie: Dostosowanie szerokości kolumn
                    miastaTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    // Aktualizacja zawartości MainContent
                    mainContent.updateContent(miastaTableView);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił problem podczas pobierania danych: " + e.getMessage());
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
