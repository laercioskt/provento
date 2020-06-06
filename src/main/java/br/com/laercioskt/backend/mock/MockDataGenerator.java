package br.com.laercioskt.backend.mock;

import br.com.laercioskt.backend.data.Availability;
import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.Product;
import br.com.laercioskt.backend.data.User;

import java.math.BigDecimal;
import java.util.*;

public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static int nextProductId = 1;
    private static int nextUserId = 1;
    
    private static final Random random = new Random(1);
    private static final String categoryNames[] = new String[]{
            "Children's books", "Best sellers", "Romance", "Mystery",
            "Thriller", "Sci-fi", "Non-fiction", "Cookbooks"};

    private static String[] word1 = new String[]{"The art of", "Mastering",
            "The secrets of", "Avoiding", "For fun and profit: ",
            "How to fail at", "10 important facts about",
            "The ultimate guide to", "Book of", "Surviving", "Encyclopedia of",
            "Very much", "Learning the basics of", "The cheap way to",
            "Being awesome at", "The life changer:", "The Vaadin way:",
            "Becoming one with", "Beginners guide to",
            "The complete visual guide to", "The mother of all references:"};

    private static String[] word2 = new String[]{"gardening",
            "living a healthy life", "designing tree houses", "home security",
            "intergalaxy travel", "meditation", "ice hockey",
            "children's education", "computer programming", "Vaadin TreeTable",
            "winter bathing", "playing the cello", "dummies", "rubber bands",
            "feeling down", "debugging", "running barefoot",
            "speaking to a big audience", "creating software", "giant needles",
            "elephants", "keeping your wife happy"};

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;

    }

    static List<Product> createProducts(List<Category> categories) {
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < 100; i++) {
            Product p = createProduct(categories);
            products.add(p);
        }

        return products;
    }
    
    
    static List<User> createUsers(List<Category> categories) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 100; i++) {
            User u = createUser(categories);
            users.add(u);
        }

        return users;
    }

    private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static Product createProduct(List<Category> categories) {
        Product p = new Product();
        p.setId(nextProductId++);
        p.setProductName(generateName());

        p.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
        p.setAvailability(Availability.values()[random.nextInt(Availability
                .values().length)]);
        if (p.getAvailability() == Availability.AVAILABLE) {
            p.setStockCount(random.nextInt(523));
        }

        p.setCategory(getCategory(categories, 1, 2));
        return p;
    }    
    
    private static User createUser(List<Category> categories) {
        User u = new User();
        u.setId(nextProductId++);
        u.setUserName(generateName());

        u.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
        u.setAvailability(Availability.values()[random.nextInt(Availability
                .values().length)]);
        if (u.getAvailability() == Availability.AVAILABLE) {
            u.setStockCount(random.nextInt(523));
        }

        u.setCategory(getCategory(categories, 1, 2));
        return u;
    }

    private static Set<Category> getCategory(List<Category> categories,
                                             int min, int max) {
        int nr = random.nextInt(max) + min;
        HashSet<Category> productCategories = new HashSet<Category>();
        for (int i = 0; i < nr; i++) {
            productCategories.add(categories.get(random.nextInt(categories
                    .size())));
        }

        return productCategories;
    }

    private static String generateName() {
        return word1[random.nextInt(word1.length)] + " "
                + word2[random.nextInt(word2.length)];
    }

}
