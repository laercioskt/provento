package br.com.laercioskt.views.protocol;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.data.Protocol;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import org.jetbrains.annotations.NotNull;

import static br.com.laercioskt.backend.data.Protocol_.*;

public class ProtocolGrid extends Grid<Protocol> {

    public ProtocolGrid() {
        setSizeFull();

        addColumn(Protocol::getCode).setHeader("Protocol code")
                .setFlexGrow(20).setSortable(true)
                .setKey(CODE);

        addColumn(p -> getCustomerLabel(p.getCustomer())).setHeader("Protocol customer")
                .setFlexGrow(20).setSortable(true)
                .setKey(CUSTOMER);

        addColumn(Protocol::getNote).setHeader("Protocol note")
                .setFlexGrow(20).setSortable(true)
                .setKey(NOTE);

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    @NotNull
    private String getCustomerLabel(Customer customer) {
        return customer.getCode() + " - " + customer.getName();
    }

    private void setColumnVisibility(int width) {
        getColumnByKey(CODE).setVisible(true);
        getColumnByKey(CUSTOMER).setVisible(true);
        getColumnByKey(NOTE).setVisible(width > 550);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> setColumnVisibility(e.getBodyClientWidth()));
    }

    public void refresh(Protocol protocol) {
        getDataCommunicator().refresh(protocol);
    }

}
