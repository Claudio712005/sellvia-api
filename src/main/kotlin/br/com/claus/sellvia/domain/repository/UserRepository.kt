package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.User

interface UserRepository {
    fun findByUsername(username: String): User?

    fun save(user: User): User

    fun existsByCpf(cpf: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean

    fun findById(id: Long): User?
}