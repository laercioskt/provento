package br.com.laercioskt.views.customer;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.data.base.ContextLookup;
import br.com.laercioskt.backend.service.CustomerService;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import static java.util.Objects.requireNonNull;

@Route(value = "Customers", layout = MainLayout.class)
public class CustomerView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Customers";

    private final CustomerGrid grid;
    private final CustomerForm form;

    private TextField filter;

    private final CustomerViewLogic viewLogic;

    private Button newCustomer;

    private final CustomerDataProvider dataProvider;

    public CustomerView() {
        CustomerService customerService = ContextLookup.getBean(CustomerService.class);
        requireNonNull(customerService,
                "it's not expected use of CustomerView without customerService instance.");

        setSizeFull();

        final HorizontalLayout topLayout = createTopBar();
        grid = new CustomerGrid();
        dataProvider = new CustomerDataProvider(customerService,
                query -> customerService.find(query, filter.getValue()).stream(),
                query -> (int) customerService.count(filter.getValue()));
        grid.setDataProvider(dataProvider);
        viewLogic = new CustomerViewLogic(this, customerService);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new CustomerForm(viewLogic);

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
        filter.setPlaceholder("Filter name");
        filter.addValueChangeListener(event -> dataProvider.refreshAll());
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newCustomer = new Button("New customer");
        newCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newCustomer.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newCustomer.addClickListener(click -> viewLogic.newCustomer());
        newCustomer.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newCustomer);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);

        return topLayout;
    }

    public void showNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewCustomerEnabled(boolean enabled) {
        newCustomer.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Customer row) {
        grid.getSelectionModel().select(row);
    }

    public void updateCustomer(Customer customer) {
        dataProvider.save(customer);
    }

    public void removeCustomer(Customer customer) {
        dataProvider.delete(customer);
    }

    public void editCustomer(Customer customer) {
        showForm(customer != null);
        form.editCustomer(customer);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

}
