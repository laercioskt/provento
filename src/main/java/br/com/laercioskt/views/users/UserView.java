package br.com.laercioskt.views.users;

import br.com.laercioskt.backend.DataService;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.mock.MockDataService;
import br.com.laercioskt.backend.service.UserService;
import br.com.laercioskt.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.atmosphere.config.service.Post;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = "Users", layout = MainLayout.class)
public class UserView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Users";

    private final UserGrid grid;
    private final UserForm form;

    private TextField filter;

    private final UserViewLogic viewLogic;

    private Button newUser;

    private final UserDataProvider dataProvider;

    public UserView(@Autowired UserService userService) {
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new UserGrid();
        dataProvider = new UserDataProvider(userService,
                query -> userService.findAll(query.getOffset(), query.getLimit(), filter.getValue()).stream(),
                query -> (int) userService.count());
        grid.setDataProvider(dataProvider);
        viewLogic = new UserViewLogic(this, userService);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new UserForm(viewLogic);
        form.setCategories(userService.getAllCategories());
        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter name, availability or category");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> dataProvider.refreshAll());
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newUser = new Button("New user");
        // Setting theme variant of new userion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newUser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newUser.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newUser.addClickListener(click -> viewLogic.newUser());
        // A shortcut to click the new user button by pressing ALT + N
        newUser.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newUser);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    /**
     * Shows a temporary popup notification to the user.
     *
     * @param msg
     * @see Notification#show(String)
     */
    public void showNotification(String msg) {
        Notification.show(msg);
    }

    /**
     * Enables/Disables the new user button.
     *
     * @param enabled
     */
    public void setNewUserEnabled(boolean enabled) {
        newUser.setEnabled(enabled);
    }

    /**
     * Deselects the selected row in the grid.
     */
    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    /**
     * Selects a row
     *
     * @param row
     */
    public void selectRow(User row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a user in the list of users.
     *
     * @param user
     */
    public void updateUser(User user) {
        dataProvider.save(user);
    }

    /**
     * Removes a user from the list of users.
     *
     * @param user
     */
    public void removeUser(User user) {
        dataProvider.delete(user);
    }

    /**
     * Displays user a form to edit a User.
     *
     * @param user
     */
    public void editUser(User user) {
        showForm(user != null);
        form.editUser(user);
    }

    /**
     * Shows and hides the new user form
     *
     * @param show
     */
    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
