package br.com.laercioskt.backend.data

import br.com.laercioskt.backend.data.UserStatus.ACTIVE
import org.amshove.kluent.`should be equal to`
import kotlin.test.Test

internal class UserTest {

    @Test
    fun `toString with all fields`() {
        val admin = Category.CategoryBuilder().withName("Admin").build()
        val user = User.UserBuilder().withUserName("Lee Sedol")
                .withPassword("Alpha_Go_2016")
                .withStatus(ACTIVE)
                .withCategory(admin).build()

        user.toString().`should be equal to`("""
            User {
                userName = Lee Sedol
                status = Active
                category = [Admin]
            }
        """.trimIndent())
    }

    @Test
    fun `toString without category`() {
        val user = User.UserBuilder().withUserName("Lee Sedol")
                .withPassword("Alpha_Go_2016")
                .withStatus(ACTIVE).build()

        user.toString().`should be equal to`("""
            User {
                userName = Lee Sedol
                status = Active
                category = []
            }
        """.trimIndent())
    }

}