package br.com.claus.sellvia.application.dto.response

data class LoginResponseDTO(
    val token: String,
    val refreshToken: String,
    val user: UserResponseDTO,
){
    var company: CompanyResponseDTO? = null
}