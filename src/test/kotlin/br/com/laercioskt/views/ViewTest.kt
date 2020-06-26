package br.com.laercioskt.views

import br.com.laercioskt.ApplicationConfigTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ApplicationConfigTest::class])
@Transactional
@Rollback
open class ViewTest {

    companion object {

        private lateinit var routes: Routes

        @BeforeAll
        @JvmStatic
        fun `discover routes`() {
            routes = Routes().autoDiscoverViews("br.com.laercioskt.views")
        }

    }

    @BeforeEach
    open fun `setup environment`() {
        MockVaadin.setup(routes)
    }

    @AfterEach
    fun `tear down`() {
        MockVaadin.tearDown()
    }

}
