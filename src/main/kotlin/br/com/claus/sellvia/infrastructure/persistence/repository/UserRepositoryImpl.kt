package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.domain.repository.UserRepository
import br.com.claus.sellvia.infrastructure.persistence.jpa.SpringDataUserRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toEntity
import br.com.claus.sellvia.infrastructure.persistence.mapper.toModel
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val springDataRepository: SpringDataUserRepository,
) : UserRepository {
    override fun findByUsername(username: String): User? {
        return springDataRepository.findByUsername(username)?.toModel()
    }

    override fun save(user: User): User {
        val entity = user.toEntity()
        val savedEntity = springDataRepository.save(entity)
        return savedEntity.toModel()
    }

    override fun existsByCpf(cpf: String): Boolean {
        return springDataRepository.existsByCpf(cpf)
    }

    override fun existsByEmail(email: String): Boolean {
        return springDataRepository.existsByEmail(email)
    }

    override fun existsByUsername(username: String): Boolean {
        return springDataRepository.existsByUsername(username)
    }

    override fun findById(id: Long): User? {
        return springDataRepository.findById(id).orElse(null)?.toModel()
    }
}