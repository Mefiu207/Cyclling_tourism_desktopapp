package com.project.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The {@code AppLayout} class represents the main layout of a JavaFX application
 * integrated with Spring Boot. This class extends {@link javafx.application.Application}
 * and is responsible for initializing the Spring context, configuring the user interface,
 * and properly shutting down the application.
 *
 * <p>In the {@code init()} method, the Spring Boot context is started using the
 * {@link MainLauncher} class, and the {@link SpringContextHolder} bean is retrieved.</p>
 *
 * <p>The {@code start(Stage stage)} method creates the main scene of the application,
 * setting the views for the left sidebar, main content, and right sidebar, and then
 * configures the window icon and title.</p>
 *
 * <p>The {@code stop()} method is responsible for closing the Spring context when
 * the application is shut down.</p>
 */
public class AppLayout extends Application {

    /**
     * The Spring Boot application context, where beans are registered.
     */
    private ConfigurableApplicationContext context;

    /**
     * Initializes the Spring Boot application context.
     * Starts Spring Boot using the {@link MainLauncher} class and retrieves the
     * {@link SpringContextHolder} bean.
     *
     * @throws Exception if an error occurs during context initialization
     */
    @Override
    public void init() throws Exception {
        context = SpringApplication.run(MainLauncher.class);
        context.getBean(SpringContextHolder.class);
        System.out.println("Spring Boot Application Started");
    }

    /**
     * Creates and configures the main scene of the JavaFX application.
     * Retrieves beans from the Spring Boot context responsible for the main content
     * and sidebars, sets them in a {@link BorderPane} layout, and then configures
     * the window icon and title.
     *
     * @param stage the main window of the JavaFX application
     */
    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        MainContent mainContent = context.getBean(MainContent.class);
        RightSidebar rightSidebar = context.getBean(RightSidebar.class);
        LeftSidebar leftSidebar = context.getBean(LeftSidebar.class);

        // Set the views in their appropriate positions in the layout
        root.setLeft(leftSidebar.getView());
        root.setCenter(mainContent.getView());
        root.setRight(rightSidebar.getView());

        // Set the window icon
        Image icon = new Image(getClass().getResourceAsStream("/com/project/ui/icon.png"));
        stage.getIcons().add(icon);

        // Configure the scene
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Turystyka rowerowa");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the Spring Boot application context when the application is shut down.
     *
     * @throws Exception if an error occurs while closing the context
     */
    @Override
    public void stop() throws Exception {
        context.close();
    }
}
