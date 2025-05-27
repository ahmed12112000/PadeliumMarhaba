package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.repositories.IGetInitRepository
import javax.inject.Inject

class GetInitUseCase @Inject constructor(private val getInitRepository: IGetInitRepository) {

    suspend fun  execute(key: String): DataResultBooking<GetInitResponse> {
        return try {
            val response = getInitRepository.getinit(key)

            if (response.isSuccessful) {
                val getInitResponse = response.body()
                if (getInitResponse != null) {
                    DataResultBooking.Success(getInitResponse)
                } else {
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (e: Exception) {
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the GetInit request")
        }
    }
}
