package br.com.laercioskt.views.customers

import br.com.laercioskt.authentication.CurrentUser
import br.com.laercioskt.backend.data.Customer
import br.com.laercioskt.backend.data.User
import br.com.laercioskt.backend.service.CustomerService
import br.com.laercioskt.views.ViewTest
import br.com.laercioskt.views.customer.CustomerView
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.should
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class CustomerViewTest : ViewTest() {

    @Autowired
    private lateinit var customerService: CustomerService

    @BeforeEach
    override fun `setup environment`() {
        super.`setup environment`()

        createCustomer("customer1", "code123456789")
        createCustomer("customer12", "code23456789")
        createCustomer("customer123", "code3456789")
        createCustomer("customer1234", "code456789")
        createCustomer("customer12345", "code56789")
    }

    @Test
    fun `navigate to Customers and check grid values`() {
        UI.getCurrent().navigate(CustomerView::class.java)

        val grid = _get<Grid<Customer>> { }
        grid.expectRows(5)

        customerService.save(createCustomer("customer123456", "code5678910"))
        UI.getCurrent().page.reload()

        grid.expectRows(6)
//        grid.expectRow(0, "customer1", "code123456789")
//        grid.expectRow(1, "customer12", "code23456789")
//        grid.expectRow(2, "customer123", "code3456789")
//        grid.expectRow(3, "customer1234", "code456789")
//        grid.expectRow(4, "customer12345", "code56789")
//        grid.expectRow(5, "customer123456", "code5678910")
    }

    @Test
    fun `navigate to Customers and create new`() {
        CurrentUser.set("admin")

        UI.getCurrent().navigate(CustomerView::class.java)

        _get<Button> { caption = "New customer" }._click()
        _get<TextField> { caption = "Customer name" }._value = "Customer 2"
        _get<TextField> { caption = "Customer code" }._value = "1234"
        _get<Button> { caption = "Save" }._click()

        UI.getCurrent().page.reload()

        val grid = _get<Grid<User>> { }
        grid.expectRows(6)
//        grid.expectRow(0, "customer1", "code123456789")
//        grid.expectRow(1, "customer12", "code23456789")
//        grid.expectRow(2, "customer123", "code3456789")
//        grid.expectRow(3, "customer1234", "code456789")
//        grid.expectRow(4, "customer12345", "code56789")
//        grid.expectRow(5, "Customer 2", "1234")
    }

    @Test
    fun `placeholder of filter field`() {
        UI.getCurrent().navigate(CustomerView::class.java)

        _get<TextField> { id = CustomerView.CUSTOMER_VIEW_FILTER_ID }
                .placeholder.`should be equal to`("Filter name or code")
    }

    private fun createCustomer(name: String, code: String) =
            customerService.save(
                    Customer.CustomerBuilder().withName(name)
                            .withCode(code)
                            .build())

}