package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.KeyMapper
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.repositories.IGetKeyRepository
import retrofit2.Response
import javax.inject.Inject


class KeyRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: KeyMapper
) : IGetKeyRepository {

    override suspend fun getReservationKey(fetchKeyRequest: FetchKeyRequest): Response<FetchKeyResponse> {
        return api.getReservationKey(mapper.FetchKeyResponseToFetchKeyResponseDTO(fetchKeyRequest))
    }
}
