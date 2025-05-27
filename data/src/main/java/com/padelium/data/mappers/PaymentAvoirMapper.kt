package com.padelium.data.mappers

import com.padelium.data.dto.UserAvoirRequestDTO
import com.padelium.domain.dto.UserAvoirRequest
import javax.inject.Inject


class PaymentAvoirMapper @Inject constructor() {

    fun PaymentAvoirRequestToPaymentAvoirRequestDTO(userAvoirRequest: UserAvoirRequest): UserAvoirRequestDTO {
        return UserAvoirRequestDTO(
            amount = userAvoirRequest.amount,
            currency = userAvoirRequest.currency,
            orderId = userAvoirRequest.orderId

        )
    }
}
