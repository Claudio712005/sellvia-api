package br.com.claus.sellvia.infrastructure.persistence.jpa

import br.com.claus.sellvia.infrastructure.persistence.model.UserEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataUserRepository : JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = ["company"])
    fun findByUsername(username: String): UserEntity?

    fun existsByUsername(username: String): Boolean

    fun existsByCpf(cpf: String): Boolean

    fun existsByEmail(email: String): Boolean
}