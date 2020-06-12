package br.com.laercioskt.backend.repository

import br.com.laercioskt.ApplicationConfigTest
import br.com.laercioskt.backend.data.Category
import br.com.laercioskt.backend.data.Category.CategoryBuilder
import br.com.laercioskt.backend.data.User.UserBuilder
import br.com.laercioskt.backend.data.UserStatus
import br.com.laercioskt.backend.data.UserStatus.ACTIVE
import br.com.laercioskt.backend.data.UserStatus.INACTIVE
import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ApplicationConfigTest::class])
internal class UserRepositoryCustomImplTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Before
    fun setup() {
        val admin = createCategory("admin")
        val users = createCategory("users")
        createUser("user1", "pass1", ACTIVE, admin)
        createUser("user12", "pass2", INACTIVE, admin)
        createUser("user123", "pass3", ACTIVE, users)
        createUser("user1234", "pass4", ACTIVE, users)
        createUser("user12345", "pass5", INACTIVE, users)
    }

    @Test
    fun countFilterTextTest() {
        userRepository.count("user").`should be equal to`(5)
        userRepository.count("user12").`should be equal to`(4)
        userRepository.count("r123").`should be equal to`(3)
        userRepository.count("4").`should be equal to`(2)
        userRepository.count("12345").`should be equal to`(1)
        userRepository.count("6").`should be equal to`(0)

        userRepository.count("TIVE").`should be equal to`(5)
    }

    private fun createCategory(name: String) =
            categoryRepository.save(
                    CategoryBuilder()
                            .withName(name).build()!!)

    private fun createUser(userName: String, password: String, status: UserStatus, admin: Category?) {
        userRepository.save(
                UserBuilder().withUserName(userName)
                        .withPassword(password)
                        .withStatus(status)
                        .withCategory(admin)
                        .build())
    }

}
