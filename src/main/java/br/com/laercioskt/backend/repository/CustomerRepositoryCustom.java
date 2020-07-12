package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface CustomerRepositoryCustom {

    List<Customer> find(String filterText, Pageable pageable, List<Order> sortOrders);

    long count(String filterText);
}
