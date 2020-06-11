package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Category;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Override
    List<Category> findAll();

}

