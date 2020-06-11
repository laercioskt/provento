package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.repository.CategoryRepository;
import br.com.laercioskt.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.springframework.data.domain.PageRequest.of;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findWithCategories(int offset, int limit, String filterText) {
        return userRepository.findWithCategories(filterText, of(offset, limit));
    }

    public long count() {
        return userRepository.count();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public Collection<Category> getAllCategories() {
        List<Category> all = categoryRepository.findAll();
        if (all.isEmpty())
            return mockCategories();
        return all;
    }

    private Collection<Category> mockCategories() {
        return range(1, 10).mapToObj(this::createCategory).collect(toList());
    }

    private Category createCategory(int i) {
        Category category = new Category();
        category.setId(i);
        category.setName("Name " + i);
        return categoryRepository.save(category);
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById((long) userId);
    }

    public long count(String filterText) {
        return userRepository.count(filterText);
    }

}
