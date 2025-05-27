package com.padelium.data.mappers

import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import javax.inject.Inject


class PaymentMapper @Inject constructor() {

    fun PaymentRequestToPaymentRequestDTO(paymentRequest: PaymentRequest): PaymentRequestDTO {
        return PaymentRequestDTO(
            amount = paymentRequest.amount,
            currency = paymentRequest.currency,
            orderId = paymentRequest.orderId
        )
    }
}

