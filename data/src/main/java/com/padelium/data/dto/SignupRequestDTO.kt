package com.padelium.data.dto

data class SignupRequestDTO(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val langKey: String = "fr"
) {
    val login: String = email
}
