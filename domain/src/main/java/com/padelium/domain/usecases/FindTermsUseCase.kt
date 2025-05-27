package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IFindTermsRepository
import okhttp3.RequestBody
import javax.inject.Inject


class FindTermsUseCase @Inject constructor(
    private val findTermsRepository: IFindTermsRepository
) {
    suspend fun FindTerms(term: RequestBody): DataResult {
        return try {
            val response = findTermsRepository.FindTerms(term)

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
