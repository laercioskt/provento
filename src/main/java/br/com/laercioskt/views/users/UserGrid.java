package br.com.laercioskt.views.users;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Grid of users, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class UserGrid extends Grid<User> {

    public UserGrid() {

        setSizeFull();

        addColumn(User::getUserName).setHeader("User name")
                .setFlexGrow(20).setSortable(true).setKey("username");

        // Format and add " €" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        addColumn(user -> decimalFormat.format(user.getPrice()) + " €")
                .setHeader("Price").setTextAlign(ColumnTextAlign.END)
                .setComparator(Comparator.comparing(User::getPrice))
                .setFlexGrow(3).setKey("price");

        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css
        // and are
        // used here in availabilityTemplate.
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        addColumn(TemplateRenderer.<User>of(availabilityTemplate)
                .withProperty("availability",
                        user -> user.getAvailability().toString()))
                .setHeader("Availability")
                .setComparator(Comparator
                        .comparing(User::getAvailability))
                .setFlexGrow(5).setKey("availability");

        addColumn(user -> user.getStockCount() == 0 ? "-"
                : Integer.toString(user.getStockCount()))
                .setHeader("Stock count")
                .setTextAlign(ColumnTextAlign.END)
                .setComparator(
                        Comparator.comparingInt(User::getStockCount))
                .setFlexGrow(3).setKey("stock");

        // Show all categories the user is in, separated by commas
        addColumn(this::formatCategories).setHeader("Category").setFlexGrow(12)
                .setKey("category");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("username").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(true);
            getColumnByKey("stock").setVisible(true);
            getColumnByKey("category").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("username").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(true);
        } else {
            getColumnByKey("username").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(false);
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

    public User getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(User user) {
        getDataCommunicator().refresh(user);
    }

    private String formatCategories(User user) {
        if (user.getCategory() == null || user.getCategory().isEmpty()) {
            return "";
        }
        return user.getCategory().stream()
                .sorted(Comparator.comparing(Category::getId))
                .map(Category::getName).collect(Collectors.joining(", "));
    }
}
