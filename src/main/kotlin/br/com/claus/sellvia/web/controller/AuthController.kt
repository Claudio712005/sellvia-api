package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.LoginRequestDTO
import br.com.claus.sellvia.application.dto.response.LoginResponseDTO
import br.com.claus.sellvia.application.usecase.auth.LoginUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginUseCase: LoginUseCase
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val response = loginUseCase.execute(request)
        return ResponseEntity.ok(response)
    }
}