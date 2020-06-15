package br.com.laercioskt.views.users;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

import static br.com.laercioskt.backend.data.User_.*;

public class UserGrid extends Grid<User> {

    public UserGrid() {

        setSizeFull();

        addColumn(User::getUserName).setHeader("User name")
                .setFlexGrow(20).setSortable(true)
                .setKey(USER_NAME);

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        String iconHtml = """
                        <iron-icon icon="vaadin:circle" class-name="[[item.status]]"></iron-icon> [[item.status]]
                        """;
        addColumn(TemplateRenderer.<User>of(iconHtml)
                .withProperty("status", user -> user.getStatus().toString()))
                .setHeader("Status")
                .setComparator(Comparator.comparing(User::getStatus))
                .setFlexGrow(5).setKey("status");

        addColumn(this::formatCategories).setHeader("Category").setFlexGrow(12).setKey(CATEGORY);

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 550) {
            getColumnByKey(USER_NAME).setVisible(true);
            getColumnByKey(STATUS).setVisible(true);
            getColumnByKey(CATEGORY).setVisible(true);
        } else {
            getColumnByKey(USER_NAME).setVisible(true);
            getColumnByKey(STATUS).setVisible(true);
            getColumnByKey(CATEGORY).setVisible(false);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> setColumnVisibility(e.getBodyClientWidth()));
    }

    public void refresh(User user) {
        getDataCommunicator().refresh(user);
    }

    private String formatCategories(User user) {
        if (user.getCategory().isEmpty()) return "";

        return user.getCategory().stream()
                .sorted(Comparator.comparing(Category::getId))
                .map(Category::getName).collect(Collectors.joining(", "));
    }
}
