package br.com.claus.sellvia.application.port

import br.com.claus.sellvia.domain.model.User

interface TokenServicePort {
    fun generateToken(user: User): String

    fun generateRefreshToken(user: User): String

    fun validateToken(token: String): String?

    fun validateRefreshToken(token: String): String?

    fun getClaimFromToken(claim: String): String?
}