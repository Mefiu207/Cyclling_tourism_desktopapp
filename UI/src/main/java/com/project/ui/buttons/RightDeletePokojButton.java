package com.project.ui.buttons;

import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.services.PokojeService;
import com.project.ui.SpringContextHolder;

// Musimy stworzyc osobna klase dla usuwania pokoi bo klasa RightDeleteButton nie ma dostępu do PokojeService z RightSidebar
public class RightDeletePokojButton extends RightDeleteButton<Pokoje, Integer>{

    public RightDeletePokojButton(String text, CustomLeftButton<Pokoje, Integer> leftButton) {
        super(text);

        this.leftButton = leftButton;
        this.services = SpringContextHolder.getContext().getBean(PokojeService.class);
        this.clazz = services.getIdClass();

        this.setOnAction(e -> onClick());
    }
}
