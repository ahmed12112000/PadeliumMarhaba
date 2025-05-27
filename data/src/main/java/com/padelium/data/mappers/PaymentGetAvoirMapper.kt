package com.padelium.data.mappers

import com.padelium.data.dto.PaymentGetAvoirRequestDTO
import com.padelium.data.dto.PaymentParCreditRequestDTO
import com.padelium.data.dto.PaymentPartBookingRequestDTO
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import javax.inject.Inject


class PaymentGetAvoirMapper @Inject constructor() {
    fun PaymentGetAvoirRequestToPaymentGetAvoirRequestDTO(paymentGetAvoirRequest: PaymentGetAvoirRequest): PaymentGetAvoirRequestDTO {
        return PaymentGetAvoirRequestDTO(
            amount = paymentGetAvoirRequest.amount,
            paymentRef = paymentGetAvoirRequest.paymentRef,
            packId = paymentGetAvoirRequest.packId,
            orderId = paymentGetAvoirRequest.orderId,

            )
    }
}

class PaymentPartBookingMapper @Inject constructor() {
    fun PaymentPartBookingRequestToPaymentPartBookingRequestDTO(paymentGetAvoirRequest: PaymentPartBookingRequest): PaymentPartBookingRequestDTO {
        return PaymentPartBookingRequestDTO(
            privateExtrasIds = paymentGetAvoirRequest.privateExtrasIds,
            bookingId = paymentGetAvoirRequest.bookingId,
            orderId = paymentGetAvoirRequest.orderId,
            id = paymentGetAvoirRequest.id,

            )
    }
}

class PaymentParCreditMapper @Inject constructor() {
    fun PaymentParCreditRequestToPaymentParCreditRequestDTO(paymentParCreditRequest: PaymentParCreditRequest): PaymentParCreditRequestDTO {
        return PaymentParCreditRequestDTO(
            privateExtrasIds = paymentParCreditRequest.privateExtrasIds,
            id = paymentParCreditRequest.id,

            )
    }
}





