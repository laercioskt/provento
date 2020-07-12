package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

    Optional<Customer> findByName(String name);

}

