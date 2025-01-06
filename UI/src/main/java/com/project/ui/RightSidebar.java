
package com.project.ui;

import com.project.springbootjavafx.services.*;
import com.project.ui.buttons.adding.AddMiastaButton;
import com.project.springbootjavafx.models.Models;

import javafx.scene.layout.VBox;

import org.springframework.context.ConfigurableApplicationContext;


public class RightSidebar {

    private VBox view;
    private MainContent mainContent;


    public RightSidebar(ConfigurableApplicationContext context, MainContent mainContent) {

        this.mainContent = mainContent;

        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
        updateButtons("Miasta", context); // Domyślna zawartość
    }

    public VBox getView() {
        return view;
    }

    public void updateButtons(String activeTab, ConfigurableApplicationContext context) {
        view.getChildren().clear();

        switch (activeTab) {

            case "Miasta":

                MiastaService miastaService = context.getBean(MiastaService.class);

                view.getChildren().addAll(
                        new AddMiastaButton(miastaService, "Dodaj miasto", this::handleModelAdded)
                );
                break;

        }

    }

    private void handleModelAdded(Models model){
         mainContent.refreshTable();
    }
}