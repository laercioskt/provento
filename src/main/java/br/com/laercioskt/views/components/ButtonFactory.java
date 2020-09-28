package br.com.laercioskt.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

import static com.vaadin.flow.component.Key.KEY_N;
import static com.vaadin.flow.component.KeyModifier.ALT;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.icon.VaadinIcon.PLUS_CIRCLE;

public class ButtonFactory {

    public static Button newObject(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        Button newObjectButton = new Button(text);
        newObjectButton.addThemeVariants(LUMO_PRIMARY);
        newObjectButton.setIcon(PLUS_CIRCLE.create());
        newObjectButton.addClickListener(listener);
        newObjectButton.addClickShortcut(KEY_N, ALT);
        return newObjectButton;
    }

}
