package com.padelium.data.mappers

import com.padelium.data.dto.UserAvoirPayRequestDTO
import com.padelium.domain.dto.UserAvoirPayRequest
import javax.inject.Inject


class PaymentPayAvoirMapper @Inject constructor(){

    fun PaymentPayAvoirRequestToPaymentPayAvoirRequestDTO (userAvoirPayRequest: UserAvoirPayRequest): UserAvoirPayRequestDTO {
        return UserAvoirPayRequestDTO(
            amount = userAvoirPayRequest.amount,
 )
    }
    }
