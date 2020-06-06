package br.com.laercioskt.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface BaseRepository<T, ID> extends CrudRepository<T, ID> {

}