package com.project.ui.buttons.adding;

import com.project.springbootjavafx.exceptions.DuplicatedMiastoException;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.services.MiastaService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.function.Consumer;

public class AddMiastaButton extends Button {

    private MiastaService miastaService;
    private Consumer<Miasta> onMiastaAdded;


    public AddMiastaButton(MiastaService miastaService, String name, Consumer<Miasta> onMiastaAdded) {
        super(name);
        this.miastaService = miastaService;
        this.onMiastaAdded = onMiastaAdded;

        // Ustawienie akcji po kliknięciu przycisku
        this.setOnAction(e -> openAddMiastoDialog());
    }


    private void openAddMiastoDialog() {

        Dialog<Miasta> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Miasto");
        dialog.setHeaderText("Wprowadź nowe miasta:");


        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField miastoField = new TextField();
        miastoField.setPromptText("Miasto");


        grid.add(new Label("Miasto:"), 0, 1);
        grid.add(miastoField, 1, 1);


        dialog.getDialogPane().setContent(grid);

        Platform.runLater(miastoField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String miasto = miastoField.getText().trim();
                return new Miasta(miasto);
            }
            return null;
        });

        Optional<Miasta> result = dialog.showAndWait();

        result.ifPresent(miastoObj -> {
            try {
                miastaService.add(miastoObj);
                onMiastaAdded.accept(miastoObj);
//                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Miasto zostało dodane.");
            } catch (DuplicatedMiastoException ex) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Miasto o nazwie \"" + miastoObj.getMiasto() + "\" już istnieje.");
            }
        });
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


//AddMiastoButton addMiastoButton = new AddMiastoButton(
//        miastaService,
//        "Dodaj Miasto",
//        miasto -> {
//            // Kod, który odświeża tabelę miast
//            mainContent.refreshTable();
//        }
//);