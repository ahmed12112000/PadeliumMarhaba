package com.padelium.domain.dto

import java.math.BigDecimal

data class CreditErrorRequest(

    val amount: BigDecimal,
    val bookingIds: List<Long?>,
    val buyerId: Long,
    val payFromAvoir: Boolean,
    val status: Boolean,
    val token: String,
    val transactionId: Long
)
