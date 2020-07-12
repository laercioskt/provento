package br.com.laercioskt.views.customer;

import br.com.laercioskt.backend.data.Customer;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

import static br.com.laercioskt.backend.data.Customer_.CODE;
import static br.com.laercioskt.backend.data.Customer_.NAME;

public class CustomerGrid extends Grid<Customer> {

    public CustomerGrid() {
        setSizeFull();

        addColumn(Customer::getName).setHeader("Customer name")
                .setFlexGrow(20).setSortable(true)
                .setKey(NAME);

        addColumn(Customer::getCode).setHeader("Customer code")
                .setFlexGrow(20).setSortable(true)
                .setKey(CODE);

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        // TODO we'll add more fields soon
        if (width > 550) {
            getColumnByKey(NAME).setVisible(true);
            getColumnByKey(CODE).setVisible(true);
        } else {
            getColumnByKey(NAME).setVisible(true);
            getColumnByKey(CODE).setVisible(true);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> setColumnVisibility(e.getBodyClientWidth()));
    }

    public void refresh(Customer customer) {
        getDataCommunicator().refresh(customer);
    }

}
