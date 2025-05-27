package com.padelium.domain.dto

import java.math.BigDecimal

data class UserAvoirRequest(

    val amount: BigDecimal,
    val currency: String,
    val orderId: String

    )
