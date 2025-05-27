package com.padelium.domain.repositories

import com.padelium.domain.dto.GetPaymentRequest
import retrofit2.Response


interface IGetPaymentRepository {
    suspend fun GetPayment (getPaymentRequest: GetPaymentRequest): Response<Boolean>

}
