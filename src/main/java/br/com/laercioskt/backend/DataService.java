package br.com.laercioskt.backend;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.Product;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.mock.MockDataService;

import java.io.Serializable;
import java.util.Collection;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService implements Serializable {

    public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(long productId);

    public abstract Product getProductById(long productId);

    public abstract void updateCategory(Category category);

    public abstract void deleteCategory(long categoryId);
    
    public static DataService get() {
        return MockDataService.getInstance();
    }

}
