package br.com.laercioskt.backend.mock;

import br.com.laercioskt.backend.DataService;
import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.Product;
import br.com.laercioskt.backend.data.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class MockDataService extends DataService {

    private static MockDataService INSTANCE;

    private List<Product> products;
    private List<User> users;

    private List<Category> categories;
    private int nextProductId = 0;
    
    private int nextUserId = 0;
    
    private int nextCategoryId = 0;

    private MockDataService() {
        categories = MockDataGenerator.createCategories();
        products = MockDataGenerator.createProducts(categories);
        
        users = MockDataGenerator.createUsers(categories);
        
        
        nextProductId = products.size() + 1;
        nextUserId = users.size() + 1;
        nextCategoryId = categories.size() + 1;
    }

    public synchronized static DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataService();
        }
        return INSTANCE;
    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public synchronized void updateProduct(Product p) {
        if (p.getId() < 0) {
            // New product
            p.setId(nextProductId++);
            products.add(p);
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == p.getId()) {
                products.set(i, p);
                return;
            }
        }

        throw new IllegalArgumentException("No product with id " + p.getId()
                + " found");
    }

    @Override
    public synchronized Product getProductById(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return products.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateCategory(Category category) {
        if (category.getId() < 0) {
            category.setId(nextCategoryId++);
            categories.add(category);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        if (categories.removeIf(category -> category.getId() == categoryId)) {
            getAllProducts().forEach(product -> {
                product.getCategory().removeIf(category -> category.getId() == categoryId);
            });
        }
    }

    @Override
    public synchronized void deleteProduct(int productId) {
        Product p = getProductById(productId);
        if (p == null) {
            throw new IllegalArgumentException("Product with id " + productId
                    + " not found");
        }
        products.remove(p);
    }

	@Override
	public void updateUser(User u) {
		
		if (u.getId() < 0) {
            // New user
            u.setId(nextUserId++);
            users.add(u);
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == u.getId()) {
                users.set(i, u);
                return;
            }
        }

        throw new IllegalArgumentException("No user with id " + u.getId()
                + " found");

	}

	@Override
	public void deleteUser(int userId) {
		
		User u = getUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("User with id " + userId
                    + " not found");
        }
        products.remove(u);
		
	}

	@Override
	public User getUserById(int userId) {
		for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return users.get(i);
            }
        }
        return null;
	}

	@Override
	public Collection<User> getAllUsers() {
		
		return Collections.unmodifiableList(users);
		
	}
}
