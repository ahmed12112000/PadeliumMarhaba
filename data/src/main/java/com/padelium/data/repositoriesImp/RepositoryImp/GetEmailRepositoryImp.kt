package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IGetEmailRepository
import retrofit2.Response
import javax.inject.Inject


class GetEmailRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IGetEmailRepository {

    override suspend fun GetEmail(bookingIds:List<Long>): Response<Long> {
        return api.GetEmail(bookingIds)
    }
}
