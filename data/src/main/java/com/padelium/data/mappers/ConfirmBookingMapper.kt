package com.padelium.data.mappers

import com.padelium.data.dto.ConfirmBookingRequestDTO
import com.padelium.domain.dto.ConfirmBookingRequest
import javax.inject.Inject


class ConfirmBookingMapper @Inject constructor(){
    fun ConfirmBookingRequestToConfirmBookingRequestDTO (confirmBookingRequest: ConfirmBookingRequest): ConfirmBookingRequestDTO {
        return ConfirmBookingRequestDTO(
            amount = confirmBookingRequest.amount,
            couponIds = confirmBookingRequest.couponIds,
            buyerId = confirmBookingRequest.buyerId,
            status = confirmBookingRequest.status,
            token = confirmBookingRequest.token,
            transactionId = confirmBookingRequest.transactionId,
            payFromAvoir = confirmBookingRequest.payFromAvoir,
            numberOfPart = confirmBookingRequest.numberOfPart,
            privateExtrasIds = confirmBookingRequest.privateExtrasIds?: emptyList(),
            sharedExtrasIds = confirmBookingRequest.sharedExtrasIds.filterNotNull(),
            userIds = confirmBookingRequest.userIds,
            bookingIds = confirmBookingRequest.bookingIds,

 )
    }
}
