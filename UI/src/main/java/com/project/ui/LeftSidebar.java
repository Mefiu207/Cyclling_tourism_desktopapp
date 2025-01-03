// LeftSidebar.java
package com.project.ui;

import com.project.ui.buttons.CustomButton;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

import com.project.springbootjavafx.services.MiastaService;

import org.springframework.context.ConfigurableApplicationContext;

public class LeftSidebar {

    private ConfigurableApplicationContext context;

    private VBox view;

    private Consumer<String> tabSelectedListener;

    public LeftSidebar(ConfigurableApplicationContext context) {

        this.context = context;

        MiastaService miastaService = context.getBean(MiastaService.class);


        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");

        // Przyciski zakładek
        Button participantsButton = new Button("Uczestnicy");
        Button tripsButton = new Button("Wycieczki");
        Button hotelsButton = new Button("Hotele");
        Button tripTypesButton = new Button("Typy wycieczek");
        Button reportsButton = new Button("Raporty");
        CustomButton miastaButton = new CustomButton(miastaService, "Miasta");


        // Domyślnie aktywna zakładka
        participantsButton.setStyle("-fx-font-weight: bold;");

        // Obsługa kliknięć
        participantsButton.setOnAction(e -> notifyTabSelected("Uczestnicy"));
        tripsButton.setOnAction(e -> notifyTabSelected("Wycieczki"));
        hotelsButton.setOnAction(e -> notifyTabSelected("Hotele"));
        tripTypesButton.setOnAction(e -> notifyTabSelected("Typy wycieczek"));
        reportsButton.setOnAction(e -> notifyTabSelected("Raporty"));
        miastaButton.setOnAction(e -> notifyTabSelected("Miasta"));


        // Dodanie przycisków do widoku
        view.getChildren().addAll(participantsButton, tripsButton, hotelsButton, tripTypesButton, reportsButton, miastaButton);
    }

    public void setOnTabSelected(Consumer<String> listener) {
        this.tabSelectedListener = listener;
    }

    public VBox getView() { return view; }

    private void notifyTabSelected(String tabName) {
        if (tabSelectedListener != null) {
            tabSelectedListener.accept(tabName);
        }
    }
}