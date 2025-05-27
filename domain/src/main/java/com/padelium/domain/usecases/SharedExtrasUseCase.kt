package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.ISharedExtrasRepository
import javax.inject.Inject


class SharedExtrasUseCase @Inject constructor(private val sharedExtrasRepository: ISharedExtrasRepository) {

    suspend fun SharedExtras(): DataResult {
        return try {
            DataResult.Loading
            val sharedExtras = sharedExtrasRepository.SharedExtras()
            DataResult.Success(sharedExtras)
        } catch (ex: Exception) {
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during fetchExtras"
            )
        }
    }
}

