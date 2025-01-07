package com.project.ui;

import javafx.scene.layout.VBox;
import com.project.ui.buttons.CustomLeftButton;

import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.*;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@DependsOn("springContextHolder")
public class LeftSidebar {

    private VBox view;

    public LeftSidebar() {
        // Tworzenie widoku tutaj, zależności jeszcze nie są wstrzyknięte
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
    }

    @PostConstruct
    private void initialize() {
        // Inicjalizacja po wstrzyknięciu zależności
        MiastaService miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);
        HoteleService hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);

        CustomLeftButton<Miasta, String> miastaButton = new CustomLeftButton<>(
                miastaService,
                "Miasta"
        );

        CustomLeftButton<Hotele, String> hoteleButton = new CustomLeftButton<>(
                hoteleService,
                "Hotele"
        );



        view.getChildren().addAll(miastaButton, hoteleButton);
    }

    public VBox getView() {
        return view;
    }
}
