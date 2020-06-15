package br.com.laercioskt.backend.repository

import br.com.laercioskt.ApplicationConfigTest
import br.com.laercioskt.backend.data.Category
import br.com.laercioskt.backend.data.Category.CategoryBuilder
import br.com.laercioskt.backend.data.User.UserBuilder
import br.com.laercioskt.backend.data.UserStatus
import br.com.laercioskt.backend.data.UserStatus.ACTIVE
import br.com.laercioskt.backend.data.UserStatus.INACTIVE
import br.com.laercioskt.backend.data.User_.STATUS
import br.com.laercioskt.backend.data.User_.USER_NAME
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
internal class UserRepositoryCustomImplTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @BeforeTest
    fun `setup environment`() {
        val admin = createCategory("admin")
        val users = createCategory("users")
        createUser("user1", "pass123456789", ACTIVE, admin)
        createUser("user12", "pass23456789", INACTIVE, admin)
        createUser("user123", "pass3456789", ACTIVE, users)
        createUser("user1234", "pass456789", ACTIVE, users)
        createUser("user12345", "pass56789", INACTIVE, users)
    }

    @Test
    fun `count user filtering by name`() {
        userRepository.count("user").`should be equal to`(5)
        userRepository.count("user12").`should be equal to`(4)
        userRepository.count("r123").`should be equal to`(3)
        userRepository.count("4").`should be equal to`(2)
        userRepository.count("12345").`should be equal to`(1)
        userRepository.count("6").`should be equal to`(0)
    }

    @Test
    fun `count user filtering by category name`() {
        userRepository.count("admin").`should be equal to`(2)
        userRepository.count("Dmin").`should be equal to`(2)
        userRepository.count("users").`should be equal to`(3)
        userRepository.count("z").`should be equal to`(0)
    }

    @Test
    fun `count user filtering by status`() {
        userRepository.count("TIVE").`should be equal to`(5)
        userRepository.count("active").`should be equal to`(5)
        userRepository.count("inactive").`should be equal to`(2)
        userRepository.count("inexistente").`should be equal to`(0)
    }

    @Test
    fun `find user with categories ordering by name in the first page`() {
        userRepository.findWithCategories("user", of(0, 2), arrayListOf(Order(ASC, USER_NAME)))
                .`should contain same`(arrayListOf(
                        user("user1"),
                        user("user12")))
    }

    @Test
    fun `find user with categories ordering by name desc in the first page`() {
        userRepository.findWithCategories("user", of(0, 2), arrayListOf(Order(DESC, USER_NAME)))
                .`should contain same`(arrayListOf(
                        user("user12345"),
                        user("user1234")))
    }

    @Test
    fun `find user with categories ordering by name desc in the last page`() {
        userRepository.findWithCategories(
                "user", of(2, 2), arrayListOf(Order(DESC, USER_NAME)))
                .`should contain same`(arrayListOf(
                        user("user1")))
    }

    @Test
    fun `find user with categories ordering by status desc`() {
        userRepository.findWithCategories(
                "user", of(0, 5), arrayListOf(Order(DESC, STATUS)))
                .`should contain same`(arrayListOf(
                        user("user12"),
                        user("user12345"),
                        user("user1"),
                        user("user123"),
                        user("user1234")))
    }

    @Test
    fun `find user with categories with empty filter`() {
        userRepository.findWithCategories(
                "", of(0, 5), emptyList())
                .`should contain same`(arrayListOf(
                        user("user1"),
                        user("user12"),
                        user("user123"),
                        user("user1234"),
                        user("user12345")))
    }

    private fun user(userName: String) = userRepository.findByUserName(userName).get()

    private fun createCategory(name: String) =
            categoryRepository.save(
                    CategoryBuilder()
                            .withName(name).build())

    private fun createUser(userName: String, password: String, status: UserStatus, admin: Category?) {
        userRepository.save(
                UserBuilder().withUserName(userName)
                        .withPassword(password)
                        .withStatus(status)
                        .withCategory(admin)
                        .build())
    }

}
