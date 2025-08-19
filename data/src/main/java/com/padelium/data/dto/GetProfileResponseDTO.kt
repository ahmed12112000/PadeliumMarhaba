package com.padelium.data.dto

import java.math.BigDecimal

data class GetProfileResponseDTO(
    val activated: Boolean = false,
    val authorities: List<String> = emptyList(),
    val avoir: BigDecimal,
    val createdBy: String = "",
    val valcreatedDate: String = "",
    val email: String = "",
    val establishmentsIds: String = "",
    val file: String = "",
    val firstName: String = "",
    val id: Long,
    val image: String = "",
    val imageUrl: String = "",
    val isOwnerestablishmentsIds: String = "",
    val langKey: String = "",
    val lastModifiedBy: String = "",
    val lastModifiedDate: String = "",
    val lastName: String = "",
    val login: String = "",
    val phone: String = "",
    val socialMediaId: String = ""
)
