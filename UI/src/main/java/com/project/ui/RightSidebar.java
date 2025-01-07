
package com.project.ui;

import com.project.springbootjavafx.services.*;
import com.project.ui.buttons.CustomLeftButton;
import com.project.ui.buttons.RightDeleteButton;

import com.project.ui.buttons.adding.AddHoteleButton;
import com.project.ui.buttons.adding.AddMiastaButton;

import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

@Component
public class RightSidebar {

    private VBox view;


    public RightSidebar() {

        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
    }

    public VBox getView() {
        return view;
    }

    public void updateButtons(CustomLeftButton<?, ?> activeButton) {
        view.getChildren().clear();

        switch (activeButton.getText()) {

            case "Miasta":
                view.getChildren().addAll(
                        new AddMiastaButton("Dodaj miasto", activeButton),
                        new RightDeleteButton("Usuń miasto", activeButton)
                );
                break;

            case "Hotele":
                view.getChildren().addAll(
                        new AddHoteleButton("Dodaj hotele", activeButton),
                        new RightDeleteButton<>("Usuń Hotel", activeButton)
                );
                break;

        }

    }

}