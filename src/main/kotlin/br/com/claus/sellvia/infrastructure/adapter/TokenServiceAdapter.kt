package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.TokenType
import br.com.claus.sellvia.domain.model.User
import br.com.claus.sellvia.infrastructure.persistence.model.UserEntity
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Date

@Component
class TokenServiceAdapter(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-expiration}") private val tokenExpiration: Long = 3600000,
    @Value("\${jwt.refresh-token-expiration}") private val tokenRefreshExpiration: Long = tokenExpiration,
) : TokenServicePort {
    private val algorithm = Algorithm.HMAC256(secret)

    override fun generateToken(user: User) = createToken(user, TokenType.MAIN_TOKEN, tokenExpiration)

    override fun generateRefreshToken(user: User) = createToken(user, TokenType.REFRESH_TOKEN, tokenRefreshExpiration)

    override fun validateToken(token: String): String? = validate(token, TokenType.MAIN_TOKEN)

    override fun validateRefreshToken(token: String): String? = validate(token, TokenType.REFRESH_TOKEN)

    override fun getClaimFromToken(claim: String): String? {
        val principal = SecurityContextHolder.getContext().authentication?.principal as? UserEntity
        return when (claim) {
            "companyId" -> principal?.company?.id.toString()
            "role" -> principal?.role?.name
            else -> null
        }
    }

    private fun createToken(
        user: User,
        type: TokenType,
        expiration: Long,
    ): String {
        return JWT.create()
            .withSubject(user.username)
            .withClaim("role", user.role.name)
            .withClaim("type", type.name)
            .withClaim("companyId", user.company?.id)
            .withExpiresAt(Date(System.currentTimeMillis() + expiration))
            .sign(algorithm)
    }

    private fun validate(
        token: String,
        expectedType: TokenType,
    ): String? {
        return try {
            val verifier = JWT.require(algorithm).build()
            val decodedJWT = verifier.verify(token)

            val tokenType = decodedJWT.getClaim("type").asString()

            if (tokenType == expectedType.name) decodedJWT.subject else null
        } catch (e: Exception) {
            null
        }
    }
}