package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.repositories.IConfirmBookingRepository
import javax.inject.Inject


class ConfirmBookingUseCase @Inject constructor(private val confirmBookingRepository: IConfirmBookingRepository) {

    suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequest): DataResult {
        return try {
            val response = confirmBookingRepository.ConfirmBooking(confirmBookingRequest)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during login")
        }
    }
}
