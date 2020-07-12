package br.com.laercioskt.backend.data

import org.amshove.kluent.`should be equal to`
import kotlin.test.Test

internal class CustomerTest {

    @Test
    fun `toString with all fields`() {
        val customer = Customer.CustomerBuilder().withName("Lee Sedol")
                .withCode("Alpha_Go_2016")
                .build()

        customer.toString().`should be equal to`("""
            Customer {
                name = Lee Sedol
                code = Alpha_Go_2016
            }
        """.trimIndent())
    }

    @Test
    fun `toString without code`() {
        val customer = Customer.CustomerBuilder()
                .withName("Lee Sedol")
                .build()

        customer.toString().`should be equal to`("""
            Customer {
                name = Lee Sedol
                code = 
            }
        """.trimIndent())
    }

}