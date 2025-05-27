package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentAvoirMapper
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.dto.UserAvoirResponse
import com.padelium.domain.repositories.IPaymentAvoirRepository
import retrofit2.Response
import javax.inject.Inject

class PaymentAvoirRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentAvoirMapper
) : IPaymentAvoirRepository {
    override suspend fun PaymentAvoir(userAvoirRequest: UserAvoirRequest): Response<UserAvoirResponse> {
        val requestDTO = mapper.PaymentAvoirRequestToPaymentAvoirRequestDTO(userAvoirRequest)

        val responseDTO = api.PaymentAvoir(requestDTO)

        return if (responseDTO.isSuccessful) {
            responseDTO.body()?.let { dto ->
                Response.success(
                    UserAvoirResponse(
                        payUrl = dto.payUrl,
                        paymentRef = dto.paymentRef,
                        orderId = dto.orderId,
                        formUrl = dto.formUrl,
                        errorCode = dto.errorCode,
                        errorMessage = dto.errorMessage,
                        orderStatus = dto.orderStatus
                    )
                )
            } ?: Response.error(responseDTO.code(), responseDTO.errorBody()!!)
        } else {
            Response.error(responseDTO.code(), responseDTO.errorBody()!!)
        }
    }
}

