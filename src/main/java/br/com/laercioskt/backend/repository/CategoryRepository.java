package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @NotNull
    @Override
    List<Category> findAll();

    @NotNull
    Optional<Category> findByName(@NotNull String name);

}

