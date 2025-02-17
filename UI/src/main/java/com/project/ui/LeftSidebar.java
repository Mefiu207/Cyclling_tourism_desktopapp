package com.project.ui;

import javafx.scene.layout.VBox;
import com.project.ui.buttons.CustomLeftButton;
import com.project.springbootjavafx.models.*;
import com.project.springbootjavafx.services.*;
import lombok.Getter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

/**
 * The {@code LeftSidebar} class represents the left sidebar panel of the user interface.
 * It is a Spring-managed component that creates a {@link VBox} layout and populates it with
 * custom buttons associated with various services.
 *
 * <p>This class depends on the {@code springContextHolder} bean, ensuring that the Spring context
 * is available when the sidebar is initialized. The custom buttons are created using the respective
 * service beans retrieved from the Spring context.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Component} indicates that this class is a Spring component.</li>
 *   <li>{@code @DependsOn("springContextHolder")} ensures that the {@code SpringContextHolder} is initialized before this component.</li>
 *   <li>{@code @Getter} (from Lombok) automatically generates getter methods for the class fields.</li>
 * </ul>
 */
@Getter
@Component
@DependsOn("springContextHolder")
public class LeftSidebar {

    /**
     * The view representing the left sidebar layout, implemented as a JavaFX {@link VBox}.
     */
    private final VBox view;

    /**
     * Constructs a new {@code LeftSidebar} instance.
     * Initializes the {@code VBox} layout with spacing and styling.
     * Note: At the time of construction, dependency injection has not yet occurred.
     */
    public LeftSidebar() {
        // Create the view; dependencies have not been injected yet
        view = new VBox();
        view.setSpacing(10);
        view.setStyle("-fx-padding: 10; -fx-background-color: #0a6a10;");
    }

    /**
     * Initializes the {@code LeftSidebar} after all dependencies have been injected.
     * This method retrieves various service beans from the Spring context and creates
     * custom buttons for each service. These buttons are then added to the sidebar view.
     *
     * <p>The services used include:</p>
     * <ul>
     *   <li>{@link MiastaService} for cities</li>
     *   <li>{@link HoteleService} for hotels</li>
     *   <li>{@link TypyWycieczekService} for tour types</li>
     *   <li>{@link WycieczkiService} for tours</li>
     *   <li>{@link KlienciService} for clients</li>
     *   <li>{@link PokojeService} for rooms</li>
     * </ul>
     *
     * <p>After creating the buttons, they are added to the {@link VBox} view of the sidebar.</p>
     */
    @PostConstruct
    private void initialize() {

        // Retrieve service beans from the Spring context
        MiastaService miastaService = SpringContextHolder.getContext().getBean(MiastaService.class);
        HoteleService hoteleService = SpringContextHolder.getContext().getBean(HoteleService.class);
        TypyWycieczekService typyWycieczekService = SpringContextHolder.getContext().getBean(TypyWycieczekService.class);
        WycieczkiService wycieczkiService = SpringContextHolder.getContext().getBean(WycieczkiService.class);
        KlienciService klienciServices = SpringContextHolder.getContext().getBean(KlienciService.class);
        PokojeService pokojeService = SpringContextHolder.getContext().getBean(PokojeService.class);

        // Create custom buttons for the left sidebar
        CustomLeftButton<Miasta, String> miastaButton = new CustomLeftButton<>(miastaService, "Miasta");
        CustomLeftButton<Hotele, String> hoteleButton = new CustomLeftButton<>(hoteleService, "Hotele");
        CustomLeftButton<TypyWycieczek, String> typyWycieczekButton = new CustomLeftButton<>(typyWycieczekService, "Typy wycieczek");
        CustomLeftButton<Wycieczki, String> wycieczkiButton = new CustomLeftButton<>(wycieczkiService, "Wycieczki");
        CustomLeftButton<Pokoje, Integer> pokojeButton = new CustomLeftButton<>(pokojeService, "Pokoje");
        CustomLeftButton<Klienci, Integer> klienciButton = new CustomLeftButton<>(klienciServices, "Klienci");

        // Add the buttons to the sidebar view
        view.getChildren().addAll(miastaButton, hoteleButton, typyWycieczekButton, wycieczkiButton, pokojeButton, klienciButton);
    }

}
