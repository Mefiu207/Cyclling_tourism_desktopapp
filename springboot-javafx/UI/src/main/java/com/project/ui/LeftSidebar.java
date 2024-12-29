// LeftSidebar.java
package com.project.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

public class LeftSidebar {

    private VBox view;
    private Consumer<String> tabSelectedListener;

    public LeftSidebar() {
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        // Przyciski zakładek
        Button participantsButton = new Button("Uczestnicy");
        Button tripsButton = new Button("Wycieczki");
        Button hotelsButton = new Button("Hotele");
        Button tripTypesButton = new Button("Typy wycieczek");
        Button reportsButton = new Button("Raporty");

        // Domyślnie aktywna zakładka
        participantsButton.setStyle("-fx-font-weight: bold;");

        // Obsługa kliknięć
        participantsButton.setOnAction(e -> notifyTabSelected("Uczestnicy"));
        tripsButton.setOnAction(e -> notifyTabSelected("Wycieczki"));
        hotelsButton.setOnAction(e -> notifyTabSelected("Hotele"));
        tripTypesButton.setOnAction(e -> notifyTabSelected("Typy wycieczek"));
        reportsButton.setOnAction(e -> notifyTabSelected("Raporty"));

        // Dodanie przycisków do widoku
        view.getChildren().addAll(participantsButton, tripsButton, hotelsButton, tripTypesButton, reportsButton);
    }

    public VBox getView() {
        return view;
    }

    public void setOnTabSelected(Consumer<String> listener) {
        this.tabSelectedListener = listener;
    }

    private void notifyTabSelected(String tabName) {
        if (tabSelectedListener != null) {
            tabSelectedListener.accept(tabName);
        }
    }
}