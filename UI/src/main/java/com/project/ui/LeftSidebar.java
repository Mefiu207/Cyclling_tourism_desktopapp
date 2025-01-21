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

        // Dostep do servicow
        MiastaService miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);
        HoteleService hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);
        TypyWycieczekService typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        WycieczkiService wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        KlienciService klienciServices = SpringContextHolder.getContext().getBean(KlienciService.class);
        PokojeService pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);

        // Tworzenie rpzyciskow po lewej stronie
        CustomLeftButton<Miasta, String> miastaButton = new CustomLeftButton<>(miastaService, "Miasta");
        CustomLeftButton<Hotele, String> hoteleButton = new CustomLeftButton<>(hoteleService, "Hotele");
        CustomLeftButton<TypyWycieczek, String> typyWycieczekButton = new CustomLeftButton<>(typyWycieczekService, "Typy wycieczek");
        CustomLeftButton<Wycieczki, String> wycieczkiButton = new CustomLeftButton<> (wycieczkiService, "Wycieczki" );
        CustomLeftButton<Pokoje, Integer> pokojeButton = new CustomLeftButton<>(pokojeService, "Pokoje");
        CustomLeftButton<Klienci, Integer> klienciButton = new CustomLeftButton<>(klienciServices, "Klienci");


        // Dodanie przyciskow do panelu
        view.getChildren().addAll(miastaButton, hoteleButton, typyWycieczekButton, wycieczkiButton, pokojeButton, klienciButton);
    }

    public VBox getView() {
        return view;
    }
}
