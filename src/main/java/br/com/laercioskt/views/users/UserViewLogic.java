package br.com.laercioskt.views.users;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.DataService;
import br.com.laercioskt.backend.data.User;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

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

    public UserViewLogic(UserView simpleCrudView) {
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
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
     * Updates the fragment without causing InventoryViewLogic navigator to
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
     * @param userId
     */
    public void enter(String userId) {
        if (userId != null && !userId.isEmpty()) {
            if (userId.equals("new")) {
                newUser();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(userId);
                    final User user = findUser(pid);
                    view.selectRow(user);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private User findUser(int userId) {
        return DataService.get().getUserById(userId);
    }

    public void saveUser(User user) {
        final boolean newUser = user.isNewUser();
        view.clearSelection();
        view.updateUser(user);
        setFragmentParameter("");
        view.showNotification(user.getUserName()
                + (newUser ? " created" : " updated"));
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
