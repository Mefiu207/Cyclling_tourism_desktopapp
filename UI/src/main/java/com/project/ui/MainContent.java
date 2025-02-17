package com.project.ui;

import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * The {@code MainContent} class represents the main content area of the application.
 * It is a Spring-managed component that encapsulates a JavaFX {@link StackPane} to display
 * dynamic content, such as a {@link TableView}.
 *
 * <p>The default content is a text message prompting the user to select a tab from the left sidebar.
 * The class provides methods to update the displayed content with a new {@code TableView} and to scroll
 * to the bottom of the table.</p>
 *
 * <p>Annotations used:</p>
 *  <ul>
 *    <li>{@code @Component} indicates that this class is a Spring component.</li>
 *  </ul>
 */
@Component
public class MainContent {

    /**
     * The view of the main content area, implemented as a JavaFX {@link StackPane}.
     */
    @Getter
    private StackPane view;

    /**
     * The currently displayed {@link TableView} in the main content area.
     */
    private TableView<?> tabelView;

    /**
     * Constructs a new {@code MainContent} instance.
     * Initializes the {@code view} with default styling and a default message prompting the user
     * to select a tab from the left sidebar.
     */
    public MainContent() {
        view = new StackPane();
        view.setStyle("-fx-padding: 10; -fx-background-color: #0e4327;");

        // Set default message
        view.getChildren().add(new Text("Wybierz zakładkę z lewej strony"));
    }

    /**
     * Updates the main content area with the specified {@link TableView}.
     * Clears any existing content in the view and displays the new table view.
     *
     * @param <T> the type of items contained in the {@code TableView}
     * @param tableView the new {@code TableView} to be displayed
     */
    public <T> void updateContent(TableView<T> tableView) {
        this.tabelView = tableView;
        view.getChildren().clear();
        view.getChildren().add(tableView);
    }

    /**
     * Scrolls to the bottom of the currently displayed {@link TableView}.
     * This method scrolls the table view to the last item in the list.
     *
     * @param <T> the type of items contained in the {@code TableView}
     */
    public <T> void scrollToBottom(){
        tabelView.scrollTo(tabelView.getItems().size());
    }
}
