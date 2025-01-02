package com.project.ui.buttons;

import com.project.springbootjavafx.services.ServicesInterface;
import javafx.scene.control.Button;

public class CustomButton extends Button {

    private ServicesInterface services;

    public CustomButton(ServicesInterface services, String name) {
        super(name);
        this.services = services;
    }

}
