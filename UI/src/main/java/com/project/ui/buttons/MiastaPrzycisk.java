package com.project.ui.buttons;

import com.project.springbootjavafx.services.MiastaService;
import com.project.springbootjavafx.models.Miasta;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MiastaPrzycisk extends Button {

    private final MiastaService miastaService;

    public MiastaPrzycisk(MiastaService miastaService) {
        super("Miasta");
        this.miastaService = miastaService;
        setOnAction(event -> onButtonClicked());
    }

    private void onButtonClicked() {
        List<Miasta> miasta = miastaService.getAllMiasta();
    }



}
