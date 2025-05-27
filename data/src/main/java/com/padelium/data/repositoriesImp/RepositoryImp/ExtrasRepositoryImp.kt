package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.ExtrasMapper
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.repositories.IExtrasRepository
import javax.inject.Inject


class ExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: ExtrasMapper
) : IExtrasRepository {

    override suspend fun Extras(): List<ExtrasResponse> {
        val response = api.Extras()

        if (response.isSuccessful) {
            response.body()?.let { extrasResponse ->
                return mapper.ExtrasRequestToExtrasResponseDTO(extrasResponse)
            } ?: throw Exception("Extras data is empty")
        } else {
            throw Exception("Error fetching Extras: ${response.errorBody()?.string()}")
        }
    }
}




