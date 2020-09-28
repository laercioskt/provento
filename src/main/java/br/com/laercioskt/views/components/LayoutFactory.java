package br.com.laercioskt.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.START;

public class LayoutFactory {

    public static HorizontalLayout createTopBar(Component filter, Button newObjectButton) {
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newObjectButton);
        topLayout.setVerticalComponentAlignment(START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

}
