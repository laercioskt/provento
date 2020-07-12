package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.repository.CustomerRepository;
import com.vaadin.flow.data.provider.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.vaadin.flow.data.provider.SortDirection.ASCENDING;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> find(Query<Customer, Void> query, String filterText) {
        List<Order> sortOrders = query.getSortOrders().stream()
                .map(s -> new Order(s.getDirection().equals(ASCENDING) ? ASC : DESC, s.getSorted())).collect(toList());
        return customerRepository.find(filterText, of(query.getOffset(), query.getLimit()), sortOrders);
    }

    public long count() {
        return customerRepository.count();
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }

    public Optional<Customer> customerById(int customerId) {
        return customerRepository.findById((long) customerId);
    }

    public long count(String filterText) {
        return customerRepository.count(filterText);
    }

}
