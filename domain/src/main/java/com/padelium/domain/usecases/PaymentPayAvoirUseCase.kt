package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.UserAvoirPayRequest
import com.padelium.domain.repositories.IPaymentPayAvoirrepository
import java.math.BigDecimal
import javax.inject.Inject

class PaymentPayAvoirUseCase @Inject constructor(
    private val paymentPayAvoirrepository: IPaymentPayAvoirrepository
) {
    suspend fun PaymentPayAvoir(amount: BigDecimal): DataResult {
        return try {
            val response = paymentPayAvoirrepository.PaymentPayAvoir(amount)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment processing")
        }
    }
}



