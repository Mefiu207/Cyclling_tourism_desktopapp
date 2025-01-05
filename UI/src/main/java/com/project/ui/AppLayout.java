
package com.project.ui;

import com.project.ui.buttons.CustomLeftButton;
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
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        LeftSidebar leftSidebar = new LeftSidebar(context);
        RightSidebar rightSidebar = new RightSidebar();
        MainContent mainContent = new MainContent();


        // Ustawienie widoków
        root.setLeft(leftSidebar.getView());
        root.setCenter(mainContent.getView());
        root.setRight(rightSidebar.getView());

        leftSidebar.setOnTabSelected((tabName, tableView) -> {
            if ("Miasta".equals(tabName)) {
                mainContent.updateContent(tableView);
            }
        });


        // Ikona okna
        Image icon = new Image(getClass().getResourceAsStream("/com/project/ui/icon.png"));
        stage.getIcons().add(icon);


        // Ustawienia sceny
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Turystyka rowerowa");
        stage.setScene(scene);
        stage.show();

//        leftSidebar.getView().getChildren().forEach(node -> {
//            if (node instanceof CustomLeftButton<?, ?> button && "Miasta".equals(button.getText())) {
//                button.fire();
//            }
//        });

    }

    @Override
    public void stop() throws Exception {
        context.close();
    }
}