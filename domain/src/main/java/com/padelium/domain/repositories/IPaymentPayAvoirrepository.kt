package com.padelium.domain.repositories

import com.padelium.domain.dto.UserAvoirPayRequest
import retrofit2.Response
import java.math.BigDecimal

interface IPaymentPayAvoirrepository {
       suspend fun PaymentPayAvoir (amount: BigDecimal): Response<Boolean>
}