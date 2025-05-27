package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.repositories.ISaveBookingRepository
import javax.inject.Inject

class SaveBookingUseCase @Inject constructor(
    private val saveBookingRepository: ISaveBookingRepository
) {

    suspend fun SaveBooking(getBookingResponses: List<GetBookingResponse>): DataResult {
        return try {
            val response = saveBookingRepository.SaveBooking(getBookingResponses)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.isNotEmpty()) {
                    DataResult.Success(responseBody)
                } else {
                    DataResult.Success(emptyList<SaveBookingResponse>())
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(
                    exception = null,
                    errorCode = response.code(),
                    errorMessage = errorMessage
                )
            }
        } catch (ex: Exception) {
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during booking"
            )
        }
    }
}






