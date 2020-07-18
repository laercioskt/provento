package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtocolRepository extends JpaRepository<Protocol, Long>, ProtocolRepositoryCustom {

}

