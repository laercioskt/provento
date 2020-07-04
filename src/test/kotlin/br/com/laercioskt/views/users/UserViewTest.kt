package br.com.laercioskt.views.users

import br.com.laercioskt.authentication.CurrentUser
import br.com.laercioskt.backend.data.Category
import br.com.laercioskt.backend.data.Category.CategoryBuilder
import br.com.laercioskt.backend.data.User
import br.com.laercioskt.backend.data.User.UserBuilder
import br.com.laercioskt.backend.data.UserStatus
import br.com.laercioskt.backend.data.UserStatus.ACTIVE
import br.com.laercioskt.backend.data.UserStatus.INACTIVE
import br.com.laercioskt.backend.repository.CategoryRepository
import br.com.laercioskt.backend.service.UserService
import br.com.laercioskt.views.ViewTest
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class UserViewTest : ViewTest() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    override fun `setup environment`() {
        super.`setup environment`()

        val admin = createCategory("admin")
        val users = createCategory("users")
        createUser("user1", "pass123456789", ACTIVE, admin)
        createUser("user12", "pass23456789", INACTIVE, admin)
        createUser("user123", "pass3456789", ACTIVE, users)
        createUser("user1234", "pass456789", ACTIVE, users)
        createUser("user12345", "pass56789", INACTIVE, users)
    }

    @Test
    fun `navigate to Users and check grid values`() {
        UI.getCurrent().navigate(UserView::class.java)

        val grid = _get<Grid<User>> { }
        grid.expectRows(5)

        userService.save(createUser("user123456", "pass5678910", INACTIVE, category("users")))
        UI.getCurrent().page.reload()

        grid.expectRows(6)
        grid.expectRow(0, "user1", " Active ", "admin")
        grid.expectRow(1, "user12", " Inactive ", "admin")
        grid.expectRow(2, "user123", " Active ", "users")
        grid.expectRow(3, "user1234", " Active ", "users")
        grid.expectRow(4, "user12345", " Inactive ", "users")
        grid.expectRow(5, "user123456", " Inactive ", "users")
    }

    @Test
    fun `navigate to Users and create new`() {
        CurrentUser.set("admin")

        UI.getCurrent().navigate(UserView::class.java)

        _get<Button> { caption = "New user" }._click()
        _get<TextField> { caption = "User name" }._value = "new user"
        _get<TextField> { caption = "Password" }._value = "password"
        _get<Select<UserStatus>> { caption = "Status" }._value = ACTIVE
        _get<CheckboxGroup<Category>> { caption = "Categories" }._value = setOf(category("admin"))
        _get<Button> { caption = "Save" }._click()

        UI.getCurrent().page.reload()

        val grid = _get<Grid<User>> { }
        grid.expectRows(6)
        grid.expectRow(0, "user1", " Active ", "admin")
        grid.expectRow(1, "user12", " Inactive ", "admin")
        grid.expectRow(2, "user123", " Active ", "users")
        grid.expectRow(3, "user1234", " Active ", "users")
        grid.expectRow(4, "user12345", " Inactive ", "users")
        grid.expectRow(5, "new user", " Active ", "admin")
    }

    private fun category(name: String) = categoryRepository.findByName(name).orElseThrow()

    private fun createCategory(name: String) = categoryRepository.save(CategoryBuilder().withName(name).build())

    private fun createUser(userName: String, password: String, status: UserStatus, category: Category) =
            userService.save(
                    UserBuilder().withUserName(userName)
                            .withPassword(password)
                            .withStatus(status)
                            .withCategory(category)
                            .build())

}