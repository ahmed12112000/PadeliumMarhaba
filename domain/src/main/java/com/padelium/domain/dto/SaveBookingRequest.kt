package com.padelium.domain.dto


data class SaveBookingRequest(
    val bookings: List<GetBookingResponse>
)

