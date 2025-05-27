package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.repositories.IPaymentAvoirRepository
import javax.inject.Inject


class PaymentAvoirUseCase @Inject constructor(private val paymentAvoirRepository: IPaymentAvoirRepository) {

    suspend fun PaymentAvoir(userAvoirRequest: UserAvoirRequest): DataResult {
        return try {
            val response = paymentAvoirRepository.PaymentAvoir(userAvoirRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    DataResult.Success(responseBody)
                } else {
                    DataResult.Success(Unit)
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment")
        }
    }
}
