package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUserName(String userName);

}

