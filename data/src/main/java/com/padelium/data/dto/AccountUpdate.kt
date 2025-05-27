package com.padelium.data.dto

import org.threeten.bp.Instant

data class AccountUpdate(
    var id: Long,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val langKey: String = "fr",
    val activated : Boolean,
    val createdBy:Long,
    val createdDate: Instant,

    )
{
    val login: String = email
}
