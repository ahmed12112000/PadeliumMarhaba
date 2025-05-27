package com.padelium.data.repositoriesImp.RepositoryImp

import android.util.Log
import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.dto.GetProfileResponseDTO
import com.padelium.data.mappers.GetProfileMapper
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.repositories.IGetProfileRepository
import javax.inject.Inject




class GetProfileRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint
) : IGetProfileRepository {

    override suspend fun GetProfile(): GetProfileResponse {
        return try {
            val response = api.GetProfile()
            Log.d("Repository", "API Response: $response")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("Repository", "Response body: $body")

                if (body != null) {
                    body
                } else {
                    Log.e("Repository", "Response body is null")
                    throw Exception("Response body is null")
                }
            } else {
                Log.e("Repository", "API call failed with code: ${response.code()}")
                Log.e("Repository", "Error body: ${response.errorBody()?.string()}")
                throw Exception("API call failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Exception in GetProfile: ${e.message}", e)
            throw e
        }
    }
}





