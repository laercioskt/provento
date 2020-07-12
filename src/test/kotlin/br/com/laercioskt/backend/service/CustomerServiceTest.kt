package br.com.laercioskt.backend.service

import br.com.laercioskt.backend.data.Customer
import br.com.laercioskt.backend.repository.CustomerRepository
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder.asc
import org.amshove.kluent.`should be equal to`
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.Order
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CustomerServiceTest {

    @InjectMocks
    private lateinit var customerService: CustomerService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @BeforeTest
    fun `setup environment`() {
        initMocks(this)
    }

    @Test
    fun find() {
        val customer = `create and return a customer for test`()

        `when`(customerRepository.find(
                "aValue", of(1, 10), listOf(Order.asc("name"))))
                .thenReturn(listOf(customer))

        val query = Query<Customer, Void>(1, 10, asc("name").build(), null, null)
        val customers = customerService.find(query, "aValue")

        customers.size.`should be equal to`(1)
        customers[0].`should be equal to`(customer)
    }

    @Test
    fun countFilterText() {
        `when`(customerRepository.count("name")).thenReturn(3)
        customerService.count("name").`should be equal to`(3)
    }

    @Test
    fun save() {
        val customer = `create and return a customer for test`()

        customerService.save(customer)

        inOrder(customerRepository).verify<CustomerRepository>(customerRepository,
                calls(1)).save(customer)
    }

    @Test
    fun deleteUser() {
        customerService.deleteCustomer(12)

        inOrder(customerRepository)
                .verify<CustomerRepository>(customerRepository, calls(1))
                .deleteById(12)
    }

    @Test
    fun customerById() {
        val customer = `create and return a customer for test`()

        `when`(customerRepository.findById(1)).thenReturn(Optional.of(customer))

        customerService.customerById(1).get().`should be equal to`(customer)
    }

    @Test
    fun count() {
        `when`(customerRepository.count()).thenReturn(3000)

        customerService.count().`should be equal to`(3000)
    }

    private fun `create and return a customer for test`(): Customer {
        return Customer.CustomerBuilder()
                .withName("Lee Sedol")
                .withCode("Alpha_Go_2016")
                .withId(1)
                .build()
    }

}