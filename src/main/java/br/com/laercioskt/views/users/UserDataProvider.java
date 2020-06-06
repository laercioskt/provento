package br.com.laercioskt.views.users;

import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.service.UserService;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

public class UserDataProvider extends ListDataProvider<User> {

    private final UserService service;

    /** Text filter that can be changed separately. */
    private String filterText = "";

    public UserDataProvider(UserService service) {
        super((Collection<User>) service.getAllUsers());

        this.service = service;
    }

    /**
     * Store given user to the backing data service.
     *
     * @param user
     *            the updated or new user
     */
    public void save(User user) {
        final boolean newUser = user.isNewUser();

        service.updateUser(user);
        if (newUser) {
            refreshAll();
        } else {
            refreshItem(user);
        }
    }

    /**
     * Delete given user from the backing data service.
     *
     * @param user
     *            the user to be deleted
     */
    public void delete(User user) {
        service.deleteUser(user.getId());
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

        setFilter(user -> passesFilter(user.getUserName(), this.filterText)
                || passesFilter(user.getStatus(), this.filterText)
                || passesFilter(user.getCategory(), this.filterText));
    }

    @Override
    public Integer getId(User user) {
        Objects.requireNonNull(user,
                "Cannot provide an id for a null user.");

        return user.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
