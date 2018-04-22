package com.github.siosio

import org.assertj.core.api.*
import org.junit.*
import org.junit.runner.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.autoconfigure.*
import org.springframework.boot.test.context.*
import org.springframework.stereotype.*
import org.springframework.test.context.junit4.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [IntegrationTest.App::class])
class IntegrationTest {
    
    @Autowired
    private lateinit var app:App

    @Test
    fun `SecretsManagerの情報が@Valueにインジェクションできること`() {
        println("******************************")
        println(app.userName)
        println("******************************")
        Assertions.assertThat(app.userName)
                .isEqualTo("test")
    }

    @SpringBootApplication
    @Component
    open class App {

        @Value("\${username}")
        lateinit var userName: String
    }
}