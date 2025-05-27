package com.padelium.data.mappers

import com.padelium.data.dto.CreditErrorRequestDTO
import com.padelium.domain.dto.CreditErrorRequest
import javax.inject.Inject

class ErrorCreditMapper @Inject constructor() {
    fun CreditErrorRequestToCreditErrorRequestDto(creditErrorRequest: CreditErrorRequest): CreditErrorRequestDTO {
        return CreditErrorRequestDTO(
            amount = creditErrorRequest.amount,
            bookingIds = creditErrorRequest.bookingIds,
            buyerId = creditErrorRequest.buyerId,
            payFromAvoir = creditErrorRequest.payFromAvoir,
            status = creditErrorRequest . status,
            token = creditErrorRequest.token,
            transactionId = creditErrorRequest.transactionId,
        )
    }
}