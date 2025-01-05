
package com.project.ui;

import javafx.scene.control.TableView;
import com.project.ui.buttons.CustomLeftButton;
import javafx.scene.text.Text;

import javafx.scene.layout.StackPane;



public class MainContent {

    private StackPane view;


    public MainContent() {
        view = new StackPane();
        view.setStyle("-fx-padding: 10; -fx-background-color: #0e4327;");
        // Ustawienie domyślnego komunikatu
        view.getChildren().add(new Text("Wybierz zakładkę z lewej strony"));
    }

    public StackPane getView() {
        return view;
    }

    public <T> void updateContent(TableView<T> tableView) {
        view.getChildren().clear();

        if (tableView == null || tableView.getItems().isEmpty()) {
            view.getChildren().add(new Text("Brak danych do wyświetlenia."));
        } else {
            view.getChildren().add(tableView);
        }
    }
}
