package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUserName(String userName);

}

