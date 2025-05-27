package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.ConfirmBookingMapper
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.repositories.IConfirmBookingRepository
import retrofit2.Response
import javax.inject.Inject


class ConfirmBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: ConfirmBookingMapper,

    ) : IConfirmBookingRepository {
    override suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequest): Response<Boolean> {
        return api.ConfirmBooking(mapper.ConfirmBookingRequestToConfirmBookingRequestDTO(confirmBookingRequest))
    }


}
