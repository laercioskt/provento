package br.com.laercioskt.views.users;

import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.service.UserService;
import com.vaadin.flow.data.provider.CallbackDataProvider;

import java.util.Locale;
import java.util.Objects;

public class UserDataProvider extends CallbackDataProvider<User, Void> {

    private final UserService service;

    private String filterText = "";

    public UserDataProvider(UserService service, FetchCallback<User, Void> fetchCallback,
                            CountCallback<User, Void> countCallback) {
        super(fetchCallback, countCallback);
        this.service = service;
    }

    public void save(User user) {
        final boolean newUser = user.isNewUser();

        service.save(user);
        if (newUser) {
            refreshAll();
        } else {
            refreshItem(user);
        }
    }

    public void delete(User user) {
        service.deleteUser(user.getId());
        refreshAll();
    }

    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public Integer getId(User user) {
        Objects.requireNonNull(user, "Cannot provide an id for a null user.");

        return Math.toIntExact(user.getId());
    }

}
