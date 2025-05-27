package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.CreditPayMapper
import com.padelium.domain.dto.CreditPayResponse
import com.padelium.domain.repositories.ICreditPayRepository
import javax.inject.Inject

class CreditPayRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: CreditPayMapper
) : ICreditPayRepository {

    override suspend fun GetCreditPay(): List<CreditPayResponse> {
        val response = api.GetCreditPay()

        if (response.isSuccessful) {
            response.body()?.let { creditPayResponseList ->
                return mapper.CreditPayResponseToCreditPayResponseDTO(creditPayResponseList)
            } ?: throw Exception("Credit data is empty")
        } else {
            throw Exception("Error fetching Credit: ${response.errorBody()?.string()}")
        }
    }
}
