package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.GetPacksMapper
import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.repositories.IGetPacksRepository
import javax.inject.Inject

class GetPacksRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: GetPacksMapper
) : IGetPacksRepository {

    override suspend fun GetPacks(): List<GetPacksResponse> {
        val response = api.GetPacks()

        if (response.isSuccessful) {
            response.body()?.let { getPacksResponseList ->
                // Map the list of GetPacksResponse to GetPacksResponseDTO
                return mapper.GetPacksResponseToGetPacksResponseDTO(getPacksResponseList)
            } ?: throw Exception("Packs data is empty")
        } else {
            // Throw an exception with the error message from the response
            throw Exception("Error fetching packs: ${response.errorBody()?.string()}")
        }
    }
}




