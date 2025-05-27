package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.repositories.IPaymentPartRepository
import com.padelium.domain.repositories.IPaymentRepository
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: IPaymentRepository) {
    suspend fun Payment(paymentRequest: PaymentRequest): DataResult {
        return try {
            val response = paymentRepository.Payment(paymentRequest)
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

class PaymentPartUseCase @Inject constructor(private val paymentRepository: IPaymentPartRepository) {
    suspend fun PaymentPart(paymentRequest: PaymentRequest): DataResult {
        return try {
            val response = paymentRepository.PaymentPart(paymentRequest)
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
