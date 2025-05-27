package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.InitBookingMapper
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.repositories.IInitBookingRepository
import retrofit2.Response
import javax.inject.Inject


class InitBookingRepositorylmp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: InitBookingMapper,
    ) : IInitBookingRepository {
    override suspend fun InitBooking(initBookingRequest: InitBookingRequest): Response<List<InitBookingResponse>> {
        return api.InitBooking(initBookingRequest)
    }


}
