package br.com.laercioskt.views.category;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.data.Category;
import br.com.laercioskt.backend.service.CategoryService;
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
public class CategoryViewLogic implements Serializable {

    private final CategoryView view;
    private final CategoryService categoryService;

    public CategoryViewLogic(CategoryView simpleCrudView, CategoryService categoryService) {
        view = simpleCrudView;
        this.categoryService = categoryService;
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

        UI.getCurrent().navigate(CategoryView.class, fragmentParameter);
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
                newCategory();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(userId);
                    final Optional<Category> category = findCategory(pid);
                    if (category.isPresent())
                        view.selectRow(category.get());
                    else {
                        Notification.show("Categoria não cadastrada");
                        view.showForm(false);
                    }

                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<Category> findCategory(int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    public void saveUser(Category category) {
        final boolean newCategory = category.isNewCategory();
        view.clearSelection();
        view.updateCategory(category);
        setFragmentParameter("");
        view.showNotification(category.getName()
                + (newCategory ? " created" : " updated"));
    }

    public void deleteCategory(Category category) {
        view.clearSelection();
        view.removeUser(category);
        setFragmentParameter("");
        view.showNotification(category.getName() + " removed");
    }

    public void editCategory(Category category) {
        if (category == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(category.getId() + "");
        }
        view.editCategory(category);
    }

    public void newCategory() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editCategory(new Category());
    }

    public void rowSelected(Category category) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editCategory(category);
        }
    }
}
