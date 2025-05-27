package com.padelium.domain.repositories

import com.padelium.domain.dto.CreditErrorRequest
import retrofit2.Response

interface IErrorCreditRepository {
    suspend fun ErrorCredit(creditErrorRequest: CreditErrorRequest): Response<Void>
}