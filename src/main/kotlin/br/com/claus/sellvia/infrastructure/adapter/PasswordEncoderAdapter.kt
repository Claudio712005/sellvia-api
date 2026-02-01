package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.PasswordEncoderPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderAdapter(
    private val springEncoder: PasswordEncoder
) : PasswordEncoderPort {
    override fun matches(rawPassword: CharSequence, encodedPassword: String) =
        springEncoder.matches(rawPassword, encodedPassword)

    override fun encode(password: CharSequence) = springEncoder.encode(password)
}