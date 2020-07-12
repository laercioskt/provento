package br.com.laercioskt.views.customer;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.service.CustomerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

import java.io.Serializable;
import java.util.Optional;

public class CustomerViewLogic implements Serializable {

    private final CustomerView view;
    private final CustomerService customerService;

    public CustomerViewLogic(CustomerView view, CustomerService customerService) {
        this.view = view;
        this.customerService = customerService;
    }

    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewCustomerEnabled(false);
        }
    }

    public void cancelCustomer() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String customerId) {
        String fragmentParameter;
        if (customerId == null || customerId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = customerId;
        }

        UI.getCurrent().navigate(CustomerView.class, fragmentParameter);
    }

    public void enter(String customerId) {
        if (customerId != null && !customerId.isEmpty()) {
            if (customerId.equals("new")) {
                newCustomer();
            } else {
                try {
                    final int pid = Integer.parseInt(customerId);
                    final Optional<Customer> customer = findCustomer(pid);
                    if (customer.isPresent())
                        view.selectRow(customer.get());
                    else {
                        Notification.show("Cliente não cadastrado");
                        view.showForm(false);
                    }

                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<Customer> findCustomer(int customerId) {
        return customerService.customerById(customerId);
    }

    public void saveCustomer(Customer customer) {
        final boolean newCustomer = customer.isNew();
        view.clearSelection();
        view.updateCustomer(customer);
        setFragmentParameter("");
        view.showNotification(customer.getName() + (newCustomer ? " created" : " updated"));
    }

    public void deleteCustomer(Customer customer) {
        view.clearSelection();
        view.removeCustomer(customer);
        setFragmentParameter("");
        view.showNotification(customer.getName() + " removed");
    }

    public void editCustomer(Customer customer) {
        if (customer == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(customer.getId() + "");
        }
        view.editCustomer(customer);
    }

    public void newCustomer() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editCustomer(new Customer());
    }

    public void rowSelected(Customer customer) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editCustomer(customer);
        }
    }
}
