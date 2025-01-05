
// RightSidebar.java
package com.project.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class RightSidebar {

    private VBox view;

    public RightSidebar() {
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
        updateButtons("Miasta"); // Domyślna zawartość
    }

    public VBox getView() {
        return view;
    }

    public void updateButtons(String activeTab) {
        view.getChildren().clear();

        switch (activeTab) {
            case "Uczestnicy":
                view.getChildren().addAll(
                        new Button("Dodaj uczestnika"),
                        new Button("Usuń uczestnika"),
                        new Button("Edytuj uczestnika")
                );
                break;
            case "Wycieczki":
                view.getChildren().addAll(
                        new Button("Dodaj wycieczkę"),
                        new Button("Usuń wycieczkę"),
                        new Button("Szczegóły wycieczki")
                );
                break;
            case "Hotele":
                view.getChildren().addAll(
                        new Button("Dodaj hotel"),
                        new Button("Usuń hotel"),
                        new Button("Edytuj hotel")
                );
                break;
            case "Typy wycieczek":
                view.getChildren().addAll(
                        new Button("Dodaj typ"),
                        new Button("Usuń typ"),
                        new Button("Edytuj typ")
                );
                break;
            case "Raporty":
                view.getChildren().addAll(
                        new Button("Generuj raport"),
                        new Button("Eksportuj raport")
                );
                break;
            case "Miasta":
                view.getChildren().addAll(
                        new Button("Dodaj miasto"),
                        new Button("Usuń miasto")
                );
                break;
        }
    }
}