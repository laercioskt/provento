package br.com.laercioskt.views.protocol;

import br.com.laercioskt.backend.data.Protocol;
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

public class ProtocolForm extends Div {

    private final TextField note;
    private final TextField code;
    private Button save;
    private Button discard;
    private final Button delete;

    private final ProtocolViewLogic viewLogic;
    private final Binder<Protocol> binder;
    private Protocol currentProtocol;

    private boolean hasChanges = false;

    public ProtocolForm(ProtocolViewLogic viewLogic) {
        setClassName("protocol-form");

        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("protocol-form-content");
        add(content);

        this.viewLogic = viewLogic;

        note = new TextField("Protocol note");
        note.setWidth("100%");
        note.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(note);

        code = new TextField("Protocol code");
        code.setWidth("100%");
        code.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(code);

        binder = new BeanValidationBinder<>(Protocol.class);
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
            if (currentProtocol != null
                    && binder.writeBeanIfValid(currentProtocol)) {
                this.viewLogic.saveProtocol(currentProtocol);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.editProtocol(currentProtocol));

        Button cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> {
            if (hasChanges) {
                new ConfirmDialog(
                        "Please confirm",
                        "There are changes in form. Do you want to discard?",
                        "Yes", this.viewLogic::cancelProtocol).open();
            } else {
                this.viewLogic.cancelProtocol();
            }
        });
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancelProtocol())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentProtocol != null) {
                new ConfirmDialog(
                        "Please confirm",
                        "Are you sure you want to delete the protocol?",
                        "Yes", () -> this.viewLogic.deleteProtocol(currentProtocol)).open();
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editProtocol(Protocol protocol) {
        if (protocol == null) {
            protocol = new Protocol();
        }
        delete.setVisible(!protocol.isNew());
        currentProtocol = protocol;
        binder.readBean(protocol);
    }
}
