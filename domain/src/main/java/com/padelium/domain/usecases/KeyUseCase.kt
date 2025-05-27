package com.padelium.domain.usecase

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.repositories.IGetKeyRepository

import javax.inject.Inject

class KeyUseCase @Inject constructor(private val keyRepository: IGetKeyRepository) {
    suspend fun getReservationKey(fetchKeyRequest: FetchKeyRequest): DataResultBooking<FetchKeyResponse> {
        return try {
            val response = keyRepository.getReservationKey(fetchKeyRequest)

            if (response.isSuccessful) {
                val fetchKeyResponse = response.body()
                if (fetchKeyResponse != null) {
                    DataResultBooking.Success(fetchKeyResponse)
                } else {
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResultBooking.Failure(ex, null, ex.localizedMessage ?: "An error occurred during fetching key")
        }
    }
}


