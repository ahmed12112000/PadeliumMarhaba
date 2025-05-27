package com.padelium.data.mappers

import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.domain.dto.InitBookingRequest
import javax.inject.Inject

class InitBookingMapper @Inject constructor() {
    fun initBookingRequestToInitBookingRequestDto(initBookingRequest: InitBookingRequest): InitBookingRequestDTO {
        return InitBookingRequestDTO(
            key = initBookingRequest.key
        )
    }
}



