package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IBalanceRepository
import javax.inject.Inject


class BalanceUseCase @Inject constructor(private val balanceRepository: IBalanceRepository) {

    suspend fun Balance(Id: Long): DataResult {
        return try {
            val response = balanceRepository.Balance(Id)
            if (response.isSuccessful) {
                val balance = response.body()
                if (balance != null) {
                    DataResult.Success(balance)
                } else {
                    DataResult.Failure(null, response.code(), "Balance response is null")
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
