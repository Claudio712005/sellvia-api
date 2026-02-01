package br.com.claus.sellvia.infrastructure.security.service

import br.com.claus.sellvia.domain.repository.UserRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")

        return user.toEntity()
    }
}