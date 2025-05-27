package com.padelium.data.dto

import java.math.BigDecimal

data class CreditErrorRequestDTO(

    val amount: BigDecimal,
    val bookingIds: List<Long?>,
    val buyerId: Long,
    val payFromAvoir: Boolean,
    val status: Boolean,
    val token: String,
    val transactionId: Long

)
