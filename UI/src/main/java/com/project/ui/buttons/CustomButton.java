package com.project.ui.buttons;

import com.project.springbootjavafx.services.ServicesInterface;
import javafx.scene.control.Button;

import java.util.List;

public class CustomButton<T, ID> extends Button {


    private ServicesInterface<T, ID> services;


    public CustomButton(ServicesInterface<T, ID> services, String name) {
        super(name);
        this.services = services;

        this.setOnAction(e -> onClick());
    }


    private void onClick() {
        List<T> records = services.getAll();

    }
}
