package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.model.User
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryImplTest : AbstractRepositoryTest() {
    @Autowired
    private lateinit var repository: UserRepositoryImpl

    @Test
    fun `should save and find user by username`() {
        val saved = repository.save(buildUser("john_doe", "john@test.com", "111.111.111-11"))

        saved.id.shouldNotBeNull()
        val found = repository.findByUsername("john_doe")
        found.shouldNotBeNull()
        found.username shouldBe "john_doe"
        found.email shouldBe "john@test.com"
    }

    @Test
    fun `should return null when user not found by username`() {
        repository.findByUsername("nonexistent").shouldBeNull()
    }

    @Test
    fun `should find user by id`() {
        val saved = repository.save(buildUser("jane_doe", "jane@test.com", "222.222.222-22"))

        val found = repository.findById(saved.id!!)
        found.shouldNotBeNull()
        found.name shouldBe "Test User"
    }

    @Test
    fun `should return null when user not found by id`() {
        repository.findById(999L).shouldBeNull()
    }

    @Test
    fun `should detect existing CPF`() {
        repository.save(buildUser("cpf_user", "cpf@test.com", "333.333.333-33"))

        repository.existsByCpf("333.333.333-33").shouldBeTrue()
        repository.existsByCpf("999.999.999-99").shouldBeFalse()
    }

    @Test
    fun `should detect existing email`() {
        repository.save(buildUser("email_user", "unique@test.com", "444.444.444-44"))

        repository.existsByEmail("unique@test.com").shouldBeTrue()
        repository.existsByEmail("notfound@test.com").shouldBeFalse()
    }

    @Test
    fun `should detect existing username`() {
        repository.save(buildUser("existing_user", "existing@test.com", "555.555.555-55"))

        repository.existsByUsername("existing_user").shouldBeTrue()
        repository.existsByUsername("missing_user").shouldBeFalse()
    }

    private fun buildUser(
        username: String,
        email: String,
        cpf: String,
    ) = User(
        id = null,
        name = "Test User",
        username = username,
        email = email,
        cpf = cpf,
        password = "hashed_password",
        isActive = true,
        role = UserRole.COMPANY_USER,
        company = null,
    )
}
