package com.project.ui.buttons;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.PokojeService;
import com.project.ui.SpringContextHolder;

/**
 * The {@code RightDeletePokojButton} class is a specialized extension of {@link RightDeleteButton}
 * specifically designed for deleting room (Pokoje) records.
 *
 * <p>
 * This class is needed because the generic {@code RightDeleteButton} does not have direct access
 * to {@link PokojeService} from the {@code RightSidebar}. It retrieves the {@link PokojeService} bean
 * from the Spring context and uses it to perform deletion operations on {@link Pokoje} entities.
 * </p>
 *
 * @see RightDeleteButton
 */
public class RightDeletePokojButton extends RightDeleteButton<Pokoje, Integer> {

    /**
     * Constructs a new {@code RightDeletePokojButton} with the specified text and associated left sidebar button.
     *
     * @param text       the text to display on the button
     * @param leftButton the {@code CustomLeftButton} associated with room operations, providing context and services
     */
    public RightDeletePokojButton(String text, CustomLeftButton<Pokoje, Integer> leftButton) {
        super(text);
        this.leftButton = leftButton;
        this.services = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.clazz = services.getIdClass();
        this.setOnAction(e -> onClick());
    }
}
