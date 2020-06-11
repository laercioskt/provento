package br.com.laercioskt.views.category;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

public class CategoryGrid extends Grid<Category> {

    public CategoryGrid() {

        setSizeFull();

        addColumn(Category::getName).setHeader("Category Name")
                .setFlexGrow(20).setSortable(true).setKey("name");

        // Format and add " €" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        /*addColumn(User::getPassword)
                .setHeader("Password")
                .setFlexGrow(20)
                .setSortable(true)
                .setKey("password");*/

        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css
        // and are
        // used here in statusTemplate.
        /*final String statusTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.status]]\"></iron-icon> [[item.status]]";
        addColumn(TemplateRenderer.<User>of(statusTemplate)
                .withProperty("status",
                        user -> user.getStatus().toString()))
                .setHeader("Status")
                .setComparator(Comparator.comparing(User::getStatus))
                .setFlexGrow(5).setKey("status");*/

        // Show all categories the user is in, separated by commas
        /*addColumn(this::formatCategories).setHeader("Category").setFlexGrow(12)
                .setKey("category");*/

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("name").setVisible(true);
           
        } else if (width > 550) {
            getColumnByKey("name").setVisible(true);
           
        } else {
            getColumnByKey("name").setVisible(true);
           
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setColumnVisibility(e.getBodyClientWidth());
        });
    }

    public Category getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Category category) {
        getDataCommunicator().refresh(category);
    }

   /* private String formatCategories(User user) {
        if (user.getCategory() == null || user.getCategory().isEmpty()) {
            return "";
        }
        return user.getCategory().stream()
                .sorted(Comparator.comparing(Category::getId))
                .map(Category::getName).collect(Collectors.joining(", "));
    }*/
}
