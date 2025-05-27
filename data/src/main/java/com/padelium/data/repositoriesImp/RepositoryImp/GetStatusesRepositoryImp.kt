package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.GetStatusesMapper
import com.padelium.domain.dto.GetStatusesResponse
import com.padelium.domain.repositories.IGetStatusesRepository
import javax.inject.Inject


class GetStatusesRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: GetStatusesMapper
) : IGetStatusesRepository {

    override suspend fun GetStatuses(): List<GetStatusesResponse> {
        val response = api.GetStatuses()

        if (response.isSuccessful) {
            response.body()?.let { getStatusesList ->
                return mapper.mapGetStatusesResponseDTOToGetGetStatusesResponse(getStatusesList)
            } ?: throw Exception("Reservation data is empty")
        } else {
            throw Exception("Error fetching Reservation: ${response.errorBody()?.string()}")
        }
    }
}

