package br.com.laercioskt.views.customer;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.views.ConfirmDialog;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomerForm extends Div {

    private final TextField name;
    private final TextField code;
    private Button save;
    private Button discard;
    private final Button delete;

    private final CustomerViewLogic viewLogic;
    private final Binder<Customer> binder;
    private Customer currentCustomer;

    private boolean hasChanges = false;

    public CustomerForm(CustomerViewLogic viewLogic) {
        setClassName("customer-form");

        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("customer-form-content");
        add(content);

        this.viewLogic = viewLogic;

        name = new TextField("Customer name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        code = new TextField("Customer code");
        code.setWidth("100%");
        code.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(code);

        binder = new BeanValidationBinder<>(Customer.class);
        binder.bindInstanceFields(this);

        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentCustomer != null
                    && binder.writeBeanIfValid(currentCustomer)) {
                this.viewLogic.saveCustomer(currentCustomer);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.editCustomer(currentCustomer));

        Button cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> {
            if (hasChanges) {
                new ConfirmDialog(
                        "Please confirm",
                        "There are changes in form. Do you want to discard?",
                        "Yes", this.viewLogic::cancelCustomer).open();
            } else {
                this.viewLogic.cancelCustomer();
            }
        });
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancelCustomer())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentCustomer != null) {
                this.viewLogic.deleteCustomer(currentCustomer);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editCustomer(Customer customer) {
        if (customer == null) {
            customer = new Customer();
        }
        delete.setVisible(!customer.isNew());
        currentCustomer = customer;
        binder.readBean(customer);
    }
}
