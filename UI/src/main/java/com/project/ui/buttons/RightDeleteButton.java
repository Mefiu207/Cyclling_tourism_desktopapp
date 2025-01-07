package com.project.ui.buttons;

import com.project.springbootjavafx.models.Models;
import javafx.scene.control.Button;
import com.project.springbootjavafx.services.ServicesInterface;
import javafx.scene.control.Dialog;


public class RightDeleteButton extends Button {

    private CustomLeftButton<?, ?> leftButton;
    private ServicesInterface services;


    public RightDeleteButton(String text, CustomLeftButton<?, ?> leftButton) {
        super(text);

        this.leftButton = leftButton;
        this.services = leftButton.getServices();

        this.setOnAction(e -> onClick());
    }

    private void onClick() {

        Dialog<Models> dialog = new Dialog<>();
//        dialog.setTitle("Usuń " + leftButton.);
        dialog.setHeaderText("Wprowadź nowe miasta:");
    }
}
