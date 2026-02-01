package br.com.claus.sellvia.domain.repository

import br.com.claus.sellvia.domain.model.User

interface UserRepository {

    fun findByUsername(username: String): User?
    fun save(user: User): User
}