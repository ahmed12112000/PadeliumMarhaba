package com.padelium.data.dto

import java.math.BigDecimal

data class UserAvoirRequestDTO(
    val amount: BigDecimal,
    val currency: String,
    val orderId: String


)
