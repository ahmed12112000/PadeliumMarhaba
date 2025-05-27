package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.GetPaymentMapper
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.repositories.IGetPaymentRepository
import retrofit2.Response
import javax.inject.Inject


class GetPaymentRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: GetPaymentMapper,
    ) : IGetPaymentRepository {
    override suspend fun GetPayment(getPaymentRequest: GetPaymentRequest): Response<Boolean> {
        return api.GetPayment(mapper.GetPaymentRequestToGetPaymentRequestDTO(getPaymentRequest))
    }


}
