package br.com.laercioskt.views.category;

import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.data.Category.CategoryFilter;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.data.User.UserFilter;
import br.com.laercioskt.backend.service.CategoryService;
import br.com.laercioskt.backend.service.UserService;
import com.vaadin.flow.data.provider.CallbackDataProvider;

import java.util.Locale;
import java.util.Objects;

public class CategoryDataProvider extends CallbackDataProvider<Category, CategoryFilter> {

    private final CategoryService service;

    /** Text filter that can be changed separately. */
    private String filterText = "";

    public CategoryDataProvider(CategoryService service, FetchCallback<Category, CategoryFilter> fetchCallback,
                            CountCallback<Category, CategoryFilter> countCallback) {
        super(fetchCallback, countCallback);
        this.service = service;
    }

    /**
     * Store given user to the backing data service.
     *
     * @param user
     *            the updated or new user
     */
    public void save(Category category) {
        final boolean newCategory = category.isNewCAtegory();

        service.updateCategory(category);
        if (newCategory) {
            refreshAll();
        } else {
            refreshItem(category);
        }
    }

    /**
     * Delete given user from the backing data service.
     *
     * @param user
     *            the user to be deleted
     */
    public void delete(Category category) {
        service.deleteCategory(category.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for user name, availability and category.
     *
     * @param filterText
     *            the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

//        setFilter(user -> passesFilter(user.getUserName(), this.filterText)
//                || passesFilter(user.getStatus(), this.filterText)
//                || passesFilter(user.getCategory(), this.filterText));
    }

    @Override
    public Integer getId(Category category) {
        Objects.requireNonNull(category,
                "Cannot provide an id for a null category.");

        return Math.toIntExact(category.getId());
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
