package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.UpdatePasswordRequestDTO
import br.com.claus.sellvia.application.dto.request.UserRequestDTO
import br.com.claus.sellvia.application.dto.response.UserResponseDTO
import br.com.claus.sellvia.application.usecase.user.ChangePasswordUseCase
import br.com.claus.sellvia.application.usecase.user.CreateUserUseCase
import br.com.claus.sellvia.application.usecase.user.DeleteUserByIdUseCase
import br.com.claus.sellvia.application.usecase.user.FindUserByIdUseCase
import br.com.claus.sellvia.application.usecase.user.UpdateUserUseCase
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(ApiEndpoints.User.USER_ROOT)
class UserController(
    private val findUserByIdUseCase: FindUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val deleteUserByIdUseCase: DeleteUserByIdUseCase,
    private val createUserUseCase: CreateUserUseCase
) {

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(
            findUserByIdUseCase.execute(id)
        )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: UserRequestDTO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(
            updateUserUseCase.execute(id, user)
        )
    }

    @PutMapping("/password/{id}")
    fun updatePassword(@PathVariable id: Long, @RequestBody  request: UpdatePasswordRequestDTO): ResponseEntity<String> {
        return ResponseEntity.ok(
            changePasswordUseCase.execute(id, request)
        )
    }

    @DeleteMapping("{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> {
        deleteUserByIdUseCase.execute(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun create(@RequestBody user: UserRequestDTO): ResponseEntity<Unit> {
        val res = createUserUseCase.execute(user)
        val uri = URI.create(ApiEndpoints.User.USER_ROOT + "/${res.id}")
        return ResponseEntity.created(uri).build()
    }

}