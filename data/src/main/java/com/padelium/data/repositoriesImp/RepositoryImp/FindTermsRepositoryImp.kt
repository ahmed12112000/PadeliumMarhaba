package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.FindTermsResponse
import com.padelium.domain.repositories.IFindTermsRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class FindTermsRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IFindTermsRepository {

    override suspend fun FindTerms(term: RequestBody): Response<List<FindTermsResponse>> {
        return api.FindTerms(term)
    }
}
