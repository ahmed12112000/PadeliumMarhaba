package com.padelium.data.repositoriesImp.RepositoryImp


import android.util.Log
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentMapper
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.repositories.IPaymentPartRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class PaymentPartRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentMapper
) : IPaymentPartRepository {

    override suspend fun PaymentPart(paymentRequest: PaymentRequest): Response<PaymentResponse?> {
        val paymentRequestDTO = mapper.PaymentRequestToPaymentRequestDTO(paymentRequest)
        val response = api.PaymentPart(paymentRequestDTO)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                response
            } else {
                Response.success(null) // Treat as a successful response with no body
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Response.error(response.code(), response.errorBody() ?: ResponseBody.create(null, "Unknown error"))
        }
    }
}
