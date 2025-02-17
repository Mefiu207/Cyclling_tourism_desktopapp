package com.project.ui;

import com.project.springbootjavafx.models.Pokoje;
import com.project.ui.buttons.*;
import com.project.ui.buttons.adding.*;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * The {@code RightSidebar} class represents the right sidebar panel of the application's user interface.
 * It is a Spring-managed component that displays context-sensitive action buttons on the right side,
 * based on the selection made in the left sidebar.
 *
 * <p>
 * This class uses a JavaFX {@link VBox} to lay out its child nodes vertically. The sidebar's appearance is
 * configured in its constructor, and its content is updated dynamically via the {@link #updateButtons(CustomLeftButton)}
 * method.
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Component} - Marks the class as a Spring component, making it eligible for component scanning and dependency injection.</li>
 *   <li>{@code @Getter} (from Lombok) - Automatically generates getter methods for all fields, reducing boilerplate code.</li>
 * </ul>
 */
@Component
@Getter
public class RightSidebar {

    /**
     * The view of the right sidebar, implemented as a JavaFX {@link VBox}.
     */
    private VBox view;

    /**
     * Constructs a new {@code RightSidebar} instance.
     * Initializes the {@code VBox} with spacing and styling to prepare the sidebar for displaying buttons.
     */
    public RightSidebar() {
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
    }

    /**
     * Updates the buttons displayed on the right sidebar based on the active button selected in the left sidebar.
     * The method clears any existing buttons and adds a new set corresponding to the text of the provided active button.
     *
     * <p>
     * The following cases are handled:
     * </p>
     * <ul>
     *   <li><b>"Miasta"</b>: Adds buttons for adding and deleting a city.</li>
     *   <li><b>"Hotele"</b>: Adds buttons for adding and deleting a hotel.</li>
     *   <li><b>"Typy wycieczek"</b>: Adds buttons for adding and deleting a tour type, displaying tour prices,
     *       and showing tour cities.</li>
     *   <li><b>"Wycieczki"</b>: Adds buttons for adding and deleting a tour.</li>
     *   <li><b>"Pokoje"</b>: Adds buttons for adding a room, deleting a room, adding and deleting a hotel list,
     *       and displaying the hotel list.</li>
     *   <li><b>"Klienci"</b>: Adds buttons for adding and deleting a client.</li>
     * </ul>
     *
     * @param activeButton the active button from the left sidebar that determines which set of buttons to display
     */
    public void updateButtons(CustomLeftButton<?, ?> activeButton) {
        view.getChildren().clear();

        switch (activeButton.getText()) {
            case "Miasta":
                view.getChildren().addAll(
                        new AddMiastaButton("Dodaj miasto", activeButton),
                        new RightDeleteButton<>("Usuń miasto", activeButton)
                );
                break;

            case "Hotele":
                view.getChildren().addAll(
                        new AddHoteleButton("Dodaj hotele", activeButton),
                        new RightDeleteButton<>("Usuń Hotel", activeButton)
                );
                break;

            case "Typy wycieczek":
                view.getChildren().addAll(
                        new AddTypyWycieczekButton("Dodaj typ wycieczki", activeButton),
                        new RightDeleteButton<>("Usuń typ wycieczki", activeButton),
                        new RightCenyButton("Pokaż ceny wycieczki", activeButton),
                        new RightMiastaWycieczekButton("Pokaż miata wycieczki", activeButton)
                );
                break;

            case "Wycieczki":
                view.getChildren().addAll(
                        new AddWycieczkiButton("Dodaj wycieczke", activeButton),
                        new RightDeleteButton<>("Usuń wycieczkę", activeButton)
                );
                break;

            case "Pokoje":
                view.getChildren().addAll(
                        new AddPokojButton("Dodaj pokój", activeButton),
                        new RightDeletePokojButton("Usuń pokój", (CustomLeftButton<Pokoje, Integer>) activeButton),
                        new AddListaHoteliButton("Dodaj listę hoteli", activeButton),
                        new RightDeleteListaHoteliButton("Usuń listy hoteli", activeButton),
                        new ListaHoteliShow("Pokaż listę hoteli")
                );
                break;

            case "Klienci":
                view.getChildren().addAll(
                        new AddKlientButton("Dodaj klienta", activeButton),
                        new RightDeleteButton<>("Usuń klienci", activeButton)
                );
                break;
        }
    }
}
