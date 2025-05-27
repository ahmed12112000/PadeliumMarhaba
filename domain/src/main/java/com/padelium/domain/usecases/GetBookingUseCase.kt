package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.repositories.IGetBookingRepository
import javax.inject.Inject

class GetBookingUseCase @Inject constructor(private val getBookingRepository: IGetBookingRepository) {

    suspend fun execute(key: String): DataResultBooking<List<GetBookingResponse>> {
        return try {
            val response = getBookingRepository.GetBooking(key)

            if (response.isSuccessful) {
                val getBookingResponse = response.body()
                if (getBookingResponse != null) {
                    DataResultBooking.Success(getBookingResponse)
                } else {
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (e: Exception) {
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the GetBooking request")
        }
    }
}
