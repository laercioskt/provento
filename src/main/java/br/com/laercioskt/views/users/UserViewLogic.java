package br.com.laercioskt.views.users;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

import java.io.Serializable;
import java.util.Optional;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the user editor form and the data source, including
 * fetching and saving users.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class UserViewLogic implements Serializable {

    private final UserView view;
    private final UserService userService;

    public UserViewLogic(UserView simpleCrudView, UserService userService) {
        view = simpleCrudView;
        this.userService = userService;
    }

    /**
     * Does the initialization of the user view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewUserEnabled(false);
        }
    }

    public void cancelUser() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing UserViewLogic navigator to
     * change view. It actually appends the userId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual user selections.
     */
    private void setFragmentParameter(String userId) {
        String fragmentParameter;
        if (userId == null || userId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = userId;
        }

        UI.getCurrent().navigate(UserView.class, fragmentParameter);
    }

    /**
     * Opens the user form and clears its fields to make it ready for
     * entering a new user if userId is null, otherwise loads the user
     * with the given userId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param userId - user internal id
     */
    public void enter(String userId) {
        if (userId != null && !userId.isEmpty()) {
            if (userId.equals("new")) {
                newUser();
            } else {
                // Ensure this is selected even if coming directly here from login
                try {
                    final int pid = Integer.parseInt(userId);
                    final Optional<User> user = findUser(pid);
                    if (user.isPresent())
                        view.selectRow(user.get());
                    else {
                        Notification.show("Usuário não cadastrado");
                        view.showForm(false);
                    }

                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<User> findUser(int userId) {
        return userService.userById(userId);
    }

    public void saveUser(User user) {
        final boolean newUser = user.isNewUser();
        view.clearSelection();
        view.updateUser(user);
        setFragmentParameter("");
        view.showNotification(user.getUserName() + (newUser ? " created" : " updated"));
    }

    public void deleteUser(User user) {
        view.clearSelection();
        view.removeUser(user);
        setFragmentParameter("");
        view.showNotification(user.getUserName() + " removed");
    }

    public void editUser(User user) {
        if (user == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(user.getId() + "");
        }
        view.editUser(user);
    }

    public void newUser() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editUser(new User());
    }

    public void rowSelected(User user) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editUser(user);
        }
    }
}
