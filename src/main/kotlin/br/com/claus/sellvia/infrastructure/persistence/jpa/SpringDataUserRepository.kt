package br.com.claus.sellvia.infrastructure.persistence.jpa

import br.com.claus.sellvia.infrastructure.persistence.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataUserRepository: JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
}