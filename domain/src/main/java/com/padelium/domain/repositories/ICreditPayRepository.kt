package com.padelium.domain.repositories

import com.padelium.domain.dto.CreditPayResponse


interface ICreditPayRepository {
        suspend fun GetCreditPay(): List<CreditPayResponse>
}
