
package com.project.ui;

import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainContent {

    @Getter
    private StackPane view;
    private TableView<?> tabelView;

    public MainContent() {
        view = new StackPane();
        view.setStyle("-fx-padding: 10; -fx-background-color: #0e4327;");

        // Ustawienie domyślnego komunikatu
        view.getChildren().add(new Text("Wybierz zakładkę z lewej strony"));
    }


    public <T> void updateContent(TableView<T> tableView) {
        this.tabelView = tableView;
        view.getChildren().clear();
        view.getChildren().add(tableView);
    }


    // Scrolluje na dół tabeli
    public <T> void scrollToBottom(){
        tabelView.scrollTo(tabelView.getItems().size());
    }

}
