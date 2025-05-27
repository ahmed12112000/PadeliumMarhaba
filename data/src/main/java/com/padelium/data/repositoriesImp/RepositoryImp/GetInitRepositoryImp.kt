package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.repositories.IGetInitRepository
import retrofit2.Response
import javax.inject.Inject

class GetInitRepositoryImp @Inject constructor (
    private val api: PadeliumApi,

) : IGetInitRepository {

    override suspend fun getinit(key: String): Response<GetInitResponse> {
        return api.GetInit(key)
    }

}