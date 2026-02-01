package br.com.claus.sellvia.infrastructure.config

import br.com.claus.sellvia.application.port.PasswordEncoderPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.application.usecase.auth.LoginUseCase
import br.com.claus.sellvia.domain.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun loginUseCase(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoderPort,
        tokenService: TokenServicePort
    ): LoginUseCase {
        return LoginUseCase(
            userRepository,
            passwordEncoder,
            tokenService
        )
    }
}