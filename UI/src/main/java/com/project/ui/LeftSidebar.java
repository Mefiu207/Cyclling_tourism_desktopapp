package com.project.ui;


import javafx.scene.layout.VBox;
import java.util.function.BiConsumer;
import javafx.scene.control.TableView;

import org.springframework.context.ConfigurableApplicationContext;

import com.project.ui.buttons.CustomLeftButton;
import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.*;


public class LeftSidebar {


    private ConfigurableApplicationContext context;
    private VBox view;
    private BiConsumer<String, TableView<?>> tabSelectedListener;


    public LeftSidebar(ConfigurableApplicationContext context) {

        this.context = context;
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");


        // Dodawanie wszystkich @Service
        MiastaService miastaService = context.getBean(MiastaService.class);


        // Ustawianie przycisków
        CustomLeftButton<Miasta, String> miastaButton = new CustomLeftButton<>(
                miastaService,
                "Miasta",
                tableView -> notifyTabSelected("Miasta", tableView)
        );

        view.getChildren().add(miastaButton);
    }

    public void setOnTabSelected(BiConsumer<String, TableView<?>> listener) {
        this.tabSelectedListener = listener;
    }

    public VBox getView() { return view; }

    private void notifyTabSelected(String tabName, TableView<?> tableView) {
        if (tabSelectedListener != null) {
            tabSelectedListener.accept(tabName, tableView);
        }
    }
}