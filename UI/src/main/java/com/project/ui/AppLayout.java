
package com.project.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.project.springbootjavafx.services.MiastaService;


public class AppLayout extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(SpringBootConfig.class);
    }

    @Override
    public void start(Stage stage) {

        MiastaService miastaService = context.getBean(MiastaService.class);

        // Główny layout aplikacji
        BorderPane root = new BorderPane();

        // Inicjalizacja komponentów
        LeftSidebar leftSidebar = new LeftSidebar(context);
        RightSidebar rightSidebar = new RightSidebar();
        MainContent mainContent = new MainContent(miastaService);

        // Ustawienie widoków
        root.setLeft(leftSidebar.getView());
        root.setCenter(mainContent.getView());
        root.setRight(rightSidebar.getView());

        // Rejestracja listenerów
        leftSidebar.setOnTabSelected(tab -> {
            mainContent.updateContent(tab); // Zmiana widoku na środku okna
            rightSidebar.updateButtons(tab); // Aktualizacja prawego paska
        });

        // Ustawienia sceny
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Turystyka rowerowa");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }
}