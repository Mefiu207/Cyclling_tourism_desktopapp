package com.project.ui.buttons;

import com.project.ui.MainContent;
import com.project.ui.SpringContextHolder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import com.project.springbootjavafx.services.AbstractServices;
import javafx.scene.layout.GridPane;


public class RightDeleteButton<T, ID> extends Button {

    private CustomLeftButton<T, ID> leftButton;
    private AbstractServices services;

    private Class<ID> clazz;

    public RightDeleteButton(String text, CustomLeftButton<T, ID> leftButton) {
        super(text);


        this.leftButton = leftButton;
        this.services = leftButton.getServices();

        // Bierzemy tym pod jakim przechowywane jest ID z services (pozwala nam operować na ID w postaci Stringa jak i Integera)
        this.clazz = services.getIdClass();



        this.setOnAction(e -> onClick());
    }

    private void onClick() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Usuń rekord z tabeli " + leftButton.getText());
        dialog.setHeaderText("ID (lub nazwa) obiektu do usunięcia (pierwsza kolumna)");

        ButtonType deleteButtonType = new ButtonType("Usuń", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("Id");


        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(idField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButtonType) {
                return idField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(id -> {
            if (id.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "ID nie może być puste.");
                return;
            }
            try {
                if( clazz == String.class ) {
                    services.delete(id);
                }
                else {
                    services.delete(Integer.valueOf(id));
                }
                // Usunięcie rekordu
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Rekord został usunięty.");

                leftButton.onClick(); // Odświeżenie tabeli
                scrollDonw();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się usunąć rekordu: " + e.getMessage());
            }
        });
    }

    private void scrollDonw(){
        MainContent mainContent = SpringContextHolder.getContext().getBean(MainContent.class);
        mainContent.scrollToBottom();
    }

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
