package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.repositories.IGetBookingRepository
import retrofit2.Response
import javax.inject.Inject

class GetBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IGetBookingRepository {

    override suspend fun GetBooking(key: String): Response<List<GetBookingResponse>> {
        return api.GetBooking(key)

    }
}