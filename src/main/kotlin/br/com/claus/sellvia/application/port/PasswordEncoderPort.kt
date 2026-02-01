package br.com.claus.sellvia.application.port

interface PasswordEncoderPort {
    fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean
    fun encode(password: CharSequence): String?
}