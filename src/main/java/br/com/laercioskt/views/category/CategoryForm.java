package br.com.laercioskt.views.category;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.data.UserStatus;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Collection;

public class CategoryForm extends Div {

    private final VerticalLayout content;

    private final TextField name;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final CategoryViewLogic viewLogic;
    private final Binder<Category> binder;
    private Category currentCategory;

    public CategoryForm(CategoryViewLogic sampleCrudLogic) {
        setClassName("user-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("user-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        name = new TextField("Name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        binder = new BeanValidationBinder<>(Category.class);
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
            if (currentCategory != null
                    && binder.writeBeanIfValid(currentCategory)) {
                viewLogic.saveUser(currentCategory);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editCategory(currentCategory));

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
            if (currentCategory != null) {
                viewLogic.deleteCategory(currentCategory);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editCategory(Category category) {
        if (category == null) {
            category = new Category();
        }
        delete.setVisible(!category.isNewCAtegory());
        currentCategory = category;
        binder.readBean(category);
    }
}
