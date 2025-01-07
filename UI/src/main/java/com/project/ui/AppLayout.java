
package com.project.ui;

import com.project.SpringBootConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;



public class AppLayout extends Application {


    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(SpringBootConfig.class);
        context.getBean(SpringContextHolder.class);
        System.out.println("Spring Boot Application Started");
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();


        MainContent mainContent = context.getBean(MainContent.class);
        RightSidebar rightSidebar = context.getBean(RightSidebar.class);
        LeftSidebar leftSidebar = context.getBean(LeftSidebar.class);


        // Ustawienie widoków
        root.setLeft(leftSidebar.getView());
        root.setCenter(mainContent.getView());
        root.setRight(rightSidebar.getView());


        // Ikona okna
        Image icon = new Image(getClass().getResourceAsStream("/com/project/ui/icon.png"));
        stage.getIcons().add(icon);


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