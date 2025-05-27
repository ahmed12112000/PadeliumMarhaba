package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IPrivateExtrasRepository
import javax.inject.Inject


class PrivateExtrasUseCase @Inject constructor(private val privateExtrasRepository: IPrivateExtrasRepository) {
    suspend fun PrivateExtras(): DataResult {
        return try {
            DataResult.Loading

            val privateExtras = privateExtrasRepository.PrivateExtras()

            DataResult.Success(privateExtras)
        } catch (ex: Exception) {
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during fetchExtras"
            )
        }
    }
}

