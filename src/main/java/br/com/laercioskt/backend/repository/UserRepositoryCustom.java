package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findWithCategories(String filterText, Pageable pageable, List<UserOrder> sortOrders);

    long count(String filterText);
}
