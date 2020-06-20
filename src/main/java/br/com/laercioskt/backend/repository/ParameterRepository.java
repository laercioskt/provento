package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    Optional<Parameter> findByName(String name);
}
