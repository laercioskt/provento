package br.com.laercioskt.views.users;

import java.util.Collection;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import br.com.laercioskt.backend.data.Availability;
import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;

public class UserForm extends Div {

    private final VerticalLayout content;

    private final TextField userName;
    private final TextField password;
    private final TextField price;
    private final TextField stockCount;
    private final Select<Availability> availability;
    private final CheckboxGroup<Category> category;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final UserViewLogic viewLogic;
    private final Binder<User> binder;
    private User currentUser;

    public UserForm(UserViewLogic sampleCrudLogic) {
        setClassName("user-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("user-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        userName = new TextField("User name");
        userName.setWidth("100%");
        userName.setRequired(true);
        userName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(userName);

        password = new TextField("Password");
        password.setWidth("100%");
        password.setRequired(true);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(password);
        
        price = new TextField("Price");
        price.setSuffixComponent(new Span("€"));
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setValueChangeMode(ValueChangeMode.EAGER);

        stockCount = new TextField("In stock");
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        final HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, price, stockCount);
        content.add(horizontalLayout);

        availability = new Select<>();
        availability.setLabel("Availability");
        availability.setWidth("100%");
        availability.setItems(Availability.values());
        content.add(availability);

        category = new CheckboxGroup<>();
        category.setLabel("Categories");
        category.setId("category");
        category.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        content.add(category);

        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentUser != null
                    && binder.writeBeanIfValid(currentUser)) {
                viewLogic.saveUser(currentUser);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editUser(currentUser));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelUser());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelUser())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentUser != null) {
                viewLogic.deleteUser(currentUser);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void setCategories(Collection<Category> categories) {
        category.setItems(categories);
    }

    public void editUser(User user) {
        if (user == null) {
            user = new User();
        }
        delete.setVisible(!user.isNewUser());
        currentUser = user;
        binder.readBean(user);
    }
}
