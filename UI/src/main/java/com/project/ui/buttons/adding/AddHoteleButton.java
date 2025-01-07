package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongHotelCodeException;
import com.project.springbootjavafx.models.Hotele;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.HoteleService;
import com.project.springbootjavafx.services.MiastaService;
import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import com.project.ui.buttons.CustomLeftButton;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Platform;

public class AddHoteleButton extends Button {

    private final HoteleService hoteleService;
    private final MiastaService miastaService;
    private final CustomLeftButton<?, ?> leftButton;

    public AddHoteleButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.leftButton = leftButton;
        this.hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);
        this.miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);

        this.setOnAction(e -> openAddHotelDialog());
    }

    public void openAddHotelDialog() {
        // Tworzenie dialogu
        Dialog<Hotele> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Hotel");
        dialog.setHeaderText("Wprowadź informacje o nowym hotelu");

        // Ustawienie przycisków dialogu
        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Tworzenie siatki dla formularza
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Pola formularza
        TextField kodField = new TextField();
        kodField.setPromptText("Kod hotelu (6 znaków)");

        TextField nazwaField = new TextField();
        nazwaField.setPromptText("Nazwa hotelu");

        ComboBox<Miasta> miastoComboBox = new ComboBox<>();
        miastoComboBox.setPromptText("Wybierz miasto");
        miastoComboBox.setItems(FXCollections.observableArrayList(miastaService.getAll()));

        TextField adresField = new TextField();
        adresField.setPromptText("Adres");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon");

        // Dodanie pól do siatki
        grid.add(new Label("Kod:"), 0, 0);
        grid.add(kodField, 1, 0);

        grid.add(new Label("Nazwa:"), 0, 1);
        grid.add(nazwaField, 1, 1);

        grid.add(new Label("Miasto:"), 0, 2);
        grid.add(miastoComboBox, 1, 2);

        grid.add(new Label("Adres:"), 0, 3);
        grid.add(adresField, 1, 3);

        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        grid.add(new Label("Telefon:"), 0, 5);
        grid.add(telefonField, 1, 5);

        dialog.getDialogPane().setContent(grid);


        // Walidacja przycisku Dodaj
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Listener do walidacji
        kodField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        nazwaField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        miastoComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        adresField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));
        telefonField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(kodField, nazwaField, miastoComboBox, adresField, emailField, telefonField, addButton));

        // Konwersja wyniku dialogu do obiektu Hotele
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String kod = kodField.getText().trim();
                String nazwa = nazwaField.getText().trim();
                Miasta miasto = miastoComboBox.getValue();
                String adres = adresField.getText().trim();
                String email = emailField.getText().trim();
                String telefon = telefonField.getText().trim();

                // Możesz dodać dodatkową walidację tutaj, np. sprawdzenie poprawności emaila

                return new Hotele(kod, nazwa, miasto, adres, email, telefon);
            }
            return null;
        });

        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);

        // Wyświetlenie dialogu i obsługa wyniku
        dialog.showAndWait().ifPresent(hotele -> {
            try {
                hoteleService.add(hotele);
                leftButton.onClick();
                mainContent.scrollToBottom();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Hotel został dodany pomyślnie!");
                alert.showAndWait();
            } catch (WrongHotelCodeException ex) {
                exceptionAlert(ex.getMessage());
            } catch (DuplicatedEntityExceptionn ex) {
                exceptionAlert(ex.getMessage());
            } catch (Exception e) {
                exceptionAlert("Nieznany błąd");
            }
        });
    }

    private void exceptionAlert(String msg) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie udało się dodać hotelu");
        alert.setHeaderText(msg);
        alert.showAndWait();

    }

    private void validateForm(TextField kodField, TextField nazwaField, ComboBox<Miasta> miastoComboBox,
                              TextField adresField, TextField emailField, TextField telefonField, Node addButton) {
        boolean disable = kodField.getText().trim().isEmpty() ||
                nazwaField.getText().trim().isEmpty() ||
                miastoComboBox.getSelectionModel().isEmpty() ||
                adresField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                telefonField.getText().trim().isEmpty();
        addButton.setDisable(disable);
    }
}
