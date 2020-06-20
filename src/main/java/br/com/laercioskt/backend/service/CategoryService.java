package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.repository.CategoryRepository;
import br.com.laercioskt.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> findAll(int offset, int limit, String filterText) {
        return categoryRepository.findAll(PageRequest.of(offset, limit)).toList();
    }

    public long count() {
        return categoryRepository.count();
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(long id) {
    	categoryRepository.deleteById(id);
    }

    private Collection<Category> mockCategories() {
        return range(1, 10).mapToObj(this::createCategory).collect(toList());
    }

    private Category createCategory(int i) {
        Category category = new Category();
        category.setId((long) i);
        category.setName("Name " + i);
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(int catId) {
        return categoryRepository.findById((long) catId);
    }
    
    
}
