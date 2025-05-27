package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.PartnerPayResponse
import com.padelium.domain.repositories.IPartnerPayRepository
import retrofit2.Response
import javax.inject.Inject


class PartnerPayRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IPartnerPayRepository {

    override suspend fun PartnerPay(Id: Long): Response<PartnerPayResponse> {
        return api.PartnerPay(Id)
    }
}
