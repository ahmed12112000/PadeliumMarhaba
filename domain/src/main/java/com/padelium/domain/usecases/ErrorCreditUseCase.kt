package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.repositories.IErrorCreditRepository
import javax.inject.Inject

class ErrorCreditUseCase @Inject constructor(private val errorCreditRepository: IErrorCreditRepository) {
  suspend fun ErrorCredit(creditErrorRequest: CreditErrorRequest): DataResult {
        return try {
            val response = errorCreditRepository.ErrorCredit(creditErrorRequest)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during signup")
        }
    }
    }
