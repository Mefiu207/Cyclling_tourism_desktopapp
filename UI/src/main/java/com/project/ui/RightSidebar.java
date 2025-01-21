
package com.project.ui;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.*;
import com.project.ui.buttons.*;

import com.project.ui.buttons.adding.*;

import javafx.scene.layout.VBox;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RightSidebar {

    private VBox view;



    public RightSidebar() {

        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
    }


    public void updateButtons(CustomLeftButton<?, ?> activeButton) {
        view.getChildren().clear();

        switch (activeButton.getText()) {

            case "Miasta":
                view.getChildren().addAll(
                        new AddMiastaButton("Dodaj miasto", activeButton),
                        new RightDeleteButton<>("Usuń miasto", activeButton)
                );
                break;

            case "Hotele":
                view.getChildren().addAll(
                        new AddHoteleButton("Dodaj hotele", activeButton),
                        new RightDeleteButton<>("Usuń Hotel", activeButton)
                );
                break;


            case "Typy wycieczek":
                view.getChildren().addAll(
                        new AddTypyWycieczekButton("Dodaj typ wycieczki", activeButton),
                        new RightDeleteButton<>("Usuń typ wycieczki", activeButton),
                        new RightCenyButton("Pokaż ceny wycieczki", activeButton),
                        new RightMiastaWycieczekButton("Pokaż miata wycieczki", activeButton)
                );
                break;

            case "Wycieczki":
                view.getChildren().addAll(
                        new AddWycieczkiButton("Dodaj wycieczke", activeButton),
                        new RightDeleteButton<>("Usuń wycieczkę", activeButton)
                );
                break;

            case "Pokoje":
                view.getChildren().addAll(
                        new AddPokojButton("Dodaj pokój", activeButton),
                        new RightDeletePokojButton("Usuń pokój", (CustomLeftButton<Pokoje, Integer>) activeButton)
                );
                break;

            case "Klienci":
                view.getChildren().addAll(
                        new AddKlientButton("Dodaj klienta", activeButton),
                        new RightDeleteButton<>("Usuń klienci", activeButton)
                );
                break;

        }

    }

}