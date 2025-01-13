package com.project.ui.buttons.adding;

import com.project.ui.buttons.CustomLeftButton;
import javafx.scene.control.Button;

public class AddTypyWycieczekButton extends Button {


    private final CustomLeftButton<?, ?> leftButton;

    public AddTypyWycieczekButton(String name, CustomLeftButton<?, ?> leftButton) {
        super(name);

        this.leftButton = leftButton;


//        this.setOnAction(e -> openAddHotelDialog());
    }

//    public void openAddHotelDialog() {
//        Dialog
//    }
}
