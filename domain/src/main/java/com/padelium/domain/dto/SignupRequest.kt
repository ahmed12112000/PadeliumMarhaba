package com.padelium.domain.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val langKey: String = "fr"
) {
    val login: String = email
}