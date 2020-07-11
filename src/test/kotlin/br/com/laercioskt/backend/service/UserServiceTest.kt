package br.com.laercioskt.backend.service

import br.com.laercioskt.backend.data.User
import br.com.laercioskt.backend.data.UserStatus.ACTIVE
import br.com.laercioskt.backend.repository.UserRepository
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder.asc
import org.amshove.kluent.`should be equal to`
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Spy
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.Order
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class UserServiceTest {

    @Spy
    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @BeforeTest
    fun `setup environment`() {
        initMocks(this)
    }

    @Test
    fun findWithCategories() {
        val user = `create and return an user for test`()

        `when`(userRepository.findWithCategories(
                "aValue", of(1, 10), listOf(Order.asc("userName"))))
                .thenReturn(listOf(user))

        val query = Query<User, Void>(1, 10, asc("userName").build(), null, null)
        val users = userService.findWithCategories(query, "aValue")

        users.size.`should be equal to`(1)
        users[0].`should be equal to`(user)
    }

    @Test
    fun countFilterText() {
        `when`(userRepository.count("name")).thenReturn(3)
        userService.count("name").`should be equal to`(3)
    }

    @Test
    fun save() {
        val user = `create and return an user for test`()

        userService.save(user)

        inOrder(userRepository).verify<UserRepository>(userRepository, calls(1)).save(user)
    }

    @Test
    fun deleteUser() {
        userService.deleteUser(12)

        inOrder(userRepository)
                .verify<UserRepository>(userRepository, calls(1))
                .deleteById(12)
    }

    @Test
    fun userById() {
        val user = `create and return an user for test`()

        `when`(userRepository.findById(1)).thenReturn(Optional.of(user))

        userService.userById(1).get().`should be equal to`(user)
    }

    @Test
    fun count() {
        `when`(userRepository.count()).thenReturn(3000)

        userService.count().`should be equal to`(3000)
    }

    private fun `create and return an user for test`(): User {
        return User.UserBuilder()
                .withUserName("Lee Sedol")
                .withPassword("Alpha_Go_2016")
                .withStatus(ACTIVE)
                .withId(1)
                .build()
    }

}