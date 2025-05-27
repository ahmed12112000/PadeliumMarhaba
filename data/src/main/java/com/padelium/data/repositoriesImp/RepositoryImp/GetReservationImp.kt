package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.GetReservationMapper
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetReservationRepository
import javax.inject.Inject


class GetReservationImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: GetReservationMapper
) : IGetReservationRepository {

    override suspend fun GetReservation(): List<GetReservationResponse> {
        val response = api.GetReservation()

        if (response.isSuccessful) {
            response.body()?.let { getReservationList ->
                return mapper.mapGetReservationResponseDTOToGetReservationResponse(getReservationList)
            } ?: throw Exception("Reservation data is empty")
        } else {
            throw Exception("Error fetching Reservation: ${response.errorBody()?.string()}")
        }
    }
}
