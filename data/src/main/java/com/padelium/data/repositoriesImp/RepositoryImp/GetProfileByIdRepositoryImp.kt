package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.GetIDMapper
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.repositories.IGetProfileByIdRepository
import javax.inject.Inject


class GetProfileByIdRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: GetIDMapper
) : IGetProfileByIdRepository {

    override suspend fun GetProfileById(id: Long): GetReservationIDResponse {
        val response = api.GetProfileById(id)

        if (response.isSuccessful) {
            response.body()?.let { getReservationList ->
                return mapper.mapGetReservationResponseToGetReservationResponse(getReservationList)
            } ?: throw Exception("Profile data is empty")
        } else {
            throw Exception("Error fetching profile: ${response.errorBody()?.string()}")
        }
    }
}

