package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.SharedExtrasMapper
import com.padelium.domain.dto.SharedExtrasResponse
import com.padelium.domain.repositories.ISharedExtrasRepository
import javax.inject.Inject


class SharedExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: SharedExtrasMapper
) : ISharedExtrasRepository {

    override suspend fun SharedExtras(): List<SharedExtrasResponse> {
        val response = api.SharedExtras()

        if (response.isSuccessful) {
            response.body()?.let { sharedextrasResponse ->
                return mapper.SharedExtrasRequestToSharedExtrasResponseDTO(sharedextrasResponse)
            } ?: throw Exception("Extras data is empty")
        } else {
            throw Exception("Error fetching Extras: ${response.errorBody()?.string()}")
        }
    }
}

