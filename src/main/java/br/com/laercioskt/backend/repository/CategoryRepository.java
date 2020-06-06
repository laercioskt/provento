package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Category;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category, Long> {

    @Override
    List<Category> findAll();

}

