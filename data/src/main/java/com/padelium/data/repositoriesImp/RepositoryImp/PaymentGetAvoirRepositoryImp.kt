package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentGetAvoirMapper
import com.padelium.data.mappers.PaymentParCreditMapper
import com.padelium.data.mappers.PaymentPartBookingMapper
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import com.padelium.domain.repositories.IPaymentParCreditRepository
import com.padelium.domain.repositories.IPaymentPartBookingRepository
import retrofit2.Response
import javax.inject.Inject


class PaymentGetAvoirRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentGetAvoirMapper,
    ) : IPaymentGetAvoirRepository {
    override suspend fun PaymentGetAvoir (paymentGetAvoirRequuest: PaymentGetAvoirRequest): Response<Boolean> {
        return api.PaymentGetAvoir(mapper.PaymentGetAvoirRequestToPaymentGetAvoirRequestDTO(paymentGetAvoirRequuest))
    }

}

class PaymentPartBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentPartBookingMapper,
    ) : IPaymentPartBookingRepository {
    override suspend fun PaymentPartBooking (paymentPartBookingRequuest: PaymentPartBookingRequest): Response<Boolean> {
        return api.PaymentPartBooking(mapper.PaymentPartBookingRequestToPaymentPartBookingRequestDTO(paymentPartBookingRequuest))
    }
}

class PaymentParCreditRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentParCreditMapper,
    ) : IPaymentParCreditRepository {
    override suspend fun PaymentParCredit (paymentParCreditRequest: PaymentParCreditRequest): Response<Boolean> {
        return api.PaymentParCredit(mapper.PaymentParCreditRequestToPaymentParCreditRequestDTO(paymentParCreditRequest))
    }
}