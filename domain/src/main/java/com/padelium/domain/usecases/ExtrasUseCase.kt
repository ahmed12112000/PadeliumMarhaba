package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IExtrasRepository
import java.math.BigDecimal
import javax.inject.Inject

class ExtrasUseCase @Inject constructor(private val extrasRepository: IExtrasRepository) {

    suspend fun Extras(): DataResult {
        return try {
            DataResult.Loading
            val extras = extrasRepository.Extras()

            DataResult.Success(extras)
        } catch (ex: Exception) {
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during fetchExtras"
            )
        }
    }
}



