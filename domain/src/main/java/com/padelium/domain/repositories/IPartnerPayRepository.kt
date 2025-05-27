package com.padelium.domain.repositories

import com.padelium.domain.dto.PartnerPayResponse
import retrofit2.Response

interface IPartnerPayRepository {

    suspend fun PartnerPay (Id: Long): Response<PartnerPayResponse>

}