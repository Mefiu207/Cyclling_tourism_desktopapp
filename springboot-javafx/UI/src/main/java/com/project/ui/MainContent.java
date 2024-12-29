
// MainContent.java
package com.project.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MainContent {

    private StackPane view;

    public MainContent() {
        view = new StackPane();
        updateContent("Uczestnicy"); // Domyślny widok
    }

    public StackPane getView() {
        return view;
    }

    public void updateContent(String activeTab) {
        view.getChildren().clear();
        view.getChildren().add(new Text("Widok: " + activeTab));

        // TODO: Dodaj logikę do wyświetlania szczegółowych widoków dla każdej zakładki
    }
}
