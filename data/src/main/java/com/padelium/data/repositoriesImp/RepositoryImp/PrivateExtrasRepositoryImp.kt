package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.PrivateExtrasMapper
import com.padelium.domain.dto.PrivateExtrasResponse
import com.padelium.domain.repositories.IPrivateExtrasRepository
import javax.inject.Inject


class PrivateExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: PrivateExtrasMapper
) : IPrivateExtrasRepository {

    override suspend fun PrivateExtras(): List<PrivateExtrasResponse> {
        val response = api.PrivateExtras()

        if (response.isSuccessful) {
            response.body()?.let { privateextrasResponse ->
                return mapper.PrivateExtrasRequestToPrivateExtrasResponseDTO(privateextrasResponse)
            } ?: throw Exception("Extras data is empty")
        } else {
            throw Exception("Error fetching Extras: ${response.errorBody()?.string()}")
        }
    }
}

