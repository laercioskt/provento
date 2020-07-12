package br.com.laercioskt.views.customer;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.service.CustomerService;
import com.vaadin.flow.data.provider.CallbackDataProvider;

import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class CustomerDataProvider extends CallbackDataProvider<Customer, Void> {

    private final CustomerService service;

    private String filterText = "";

    public CustomerDataProvider(CustomerService service, FetchCallback<Customer, Void> fetchCallback,
                                CountCallback<Customer, Void> countCallback) {
        super(fetchCallback, countCallback);
        this.service = service;
    }

    public void save(Customer customer) {
        final boolean newCustomer = customer.isNew();

        service.save(customer);
        if (newCustomer) {
            refreshAll();
        } else {
            refreshItem(customer);
        }
    }

    public void delete(Customer customer) {
        service.deleteCustomer(customer.getId());
        refreshAll();
    }

    public void setFilter(String filterText) {
        requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public Integer getId(Customer customer) {
        requireNonNull(customer, "Cannot provide an id for a null customer.");

        return Math.toIntExact(customer.getId());
    }

}
