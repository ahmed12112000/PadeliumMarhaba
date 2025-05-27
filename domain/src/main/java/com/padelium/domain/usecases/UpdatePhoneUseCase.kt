package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IUpdatePhoneRepository
import okhttp3.RequestBody
import javax.inject.Inject


class UpdatePhoneUseCase @Inject constructor(
    private val updatePhoneRepository: IUpdatePhoneRepository
) {
    suspend fun UpdatePhone(Phone: RequestBody): DataResult {
        return try {
            val response = updatePhoneRepository.UpdatePhone(Phone)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DataResult.Success(body)
                } else {
                    DataResult.Failure(null, response.code(), "Response body is null")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during the request")
        }
    }
}
