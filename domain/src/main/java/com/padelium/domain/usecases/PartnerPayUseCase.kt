package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IPartnerPayRepository
import javax.inject.Inject


class PartnerPayUseCase @Inject constructor(private val partnerPayRepository: IPartnerPayRepository) {

    suspend fun PartnerPay(Id: Long): DataResult {
        return try {
            val response = partnerPayRepository.PartnerPay(Id)
            if (response.isSuccessful) {
                val partnerPayResponse = response.body()
                if (partnerPayResponse != null) {
                    DataResult.Success(partnerPayResponse)
                } else {
                    DataResult.Failure(null, response.code(), "Response body is null")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during balance fetching")
        }
    }
}