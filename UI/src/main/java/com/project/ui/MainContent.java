
package com.project.ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import com.project.springbootjavafx.services.MiastaService;
import com.project.springbootjavafx.models.Miasta;


public class MainContent {

    private StackPane view;
    private MiastaService miastaService;


    public MainContent(MiastaService miastaService) {
        this.miastaService = miastaService;
        view = new StackPane();
        updateContent("Uczestnicy"); // Domyślny widok
        view.setStyle("-fx-padding: 10; -fx-background-color: #0e4327;");
    }

    public StackPane getView() {
        return view;
    }

    public void updateContent(String activeTab) {

        view.getChildren().clear();

        if (!"Miasta".equals(activeTab)) {
            view.getChildren().add(new Text("Widok: " + activeTab));
            return;
        }

        ListView<String> listView = new ListView<>();
        List<Miasta> lista = miastaService.getAll();


        List<String> nazwyMiast = lista.stream()
                .map(Miasta::getMiasto)
                .toList();

        listView.setItems(FXCollections.observableArrayList(nazwyMiast));
        view.getChildren().add(listView);

        // TODO: Dodaj logikę do wyświetlania szczegółowych widoków dla każdej zakładki
    }
}
