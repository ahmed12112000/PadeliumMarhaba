package com.padelium.domain.repositories

import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.ConfirmBookingResponse
import retrofit2.Response


interface IConfirmBookingRepository {
    suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequest): Response<Boolean>

}
