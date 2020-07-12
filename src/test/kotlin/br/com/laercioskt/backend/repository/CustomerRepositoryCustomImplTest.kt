package br.com.laercioskt.backend.repository

import br.com.laercioskt.ApplicationConfigTest
import br.com.laercioskt.backend.data.Customer
import br.com.laercioskt.backend.data.Customer_.NAME
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.Order
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ApplicationConfigTest::class])
@Transactional
@Rollback
internal class CustomerRepositoryCustomImplTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeTest
    fun `setup environment`() {
        createCustomer("customer1", "code123456789")
        createCustomer("customer12", "code23456789")
        createCustomer("customer123", "code3456789")
        createCustomer("customer1234", "code456789")
        createCustomer("customer12345", "code56789")
    }

    @Test
    fun `count customer filtering by name and code`() {
        customerRepository.count("code").`should be equal to`(5)
        customerRepository.count("4567").`should be equal to`(4)
        customerRepository.count("345").`should be equal to`(4)
        customerRepository.count("2").`should be equal to`(5)
        customerRepository.count("12345").`should be equal to`(2)
        customerRepository.count("10").`should be equal to`(0)
    }

    @Test
    fun `find customer ordering by name in the first page`() {
        customerRepository.find("customer", of(0, 2), arrayListOf(Order(ASC, NAME)))
                .`should contain same`(arrayListOf(
                        customer("customer1"),
                        customer("customer12")))
    }

    @Test
    fun `find customer ordering by name desc in the first page`() {
        customerRepository.find("customer", of(0, 2), arrayListOf(Order(DESC, NAME)))
                .`should contain same`(arrayListOf(
                        customer("customer12345"),
                        customer("customer1234")))
    }

    @Test
    fun `find customer ordering by name desc in the last page`() {
        customerRepository.find(
                "customer", of(2, 2), arrayListOf(Order(DESC, NAME)))
                .`should contain same`(arrayListOf(
                        customer("customer1")))
    }

    @Test
    fun `find customer ordering by code desc in the last page`() {
        customerRepository.find(
                "customer", of(2, 2), arrayListOf(Order(DESC, NAME)))
                .`should contain same`(arrayListOf(
                        customer("customer1")))
    }

    @Test
    fun `find customer with empty filter`() {
        customerRepository.find(
                "", of(0, 5), emptyList())
                .`should contain same`(arrayListOf(
                        customer("customer1"),
                        customer("customer12"),
                        customer("customer123"),
                        customer("customer1234"),
                        customer("customer12345")))
    }

    private fun customer(name: String) = customerRepository.findByName(name).get()

    private fun createCustomer(name: String, code: String) {
        customerRepository.save(
                Customer.CustomerBuilder().withName(name)
                        .withCode(code)
                        .build())
    }

}
