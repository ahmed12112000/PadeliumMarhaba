package com.padelium.data.mappers


import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.GetBookingResponse
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

class GetBookingMapper @Inject constructor() {
    fun GetBookingResponseToGetBookingResponseDto(getBookingResponse: List<GetBookingResponse>): List<GetBookingResponseDTO> {

        return getBookingResponse.map { response ->

            GetBookingResponseDTO(
                establishmentDTO = response.establishmentDTO,
                amount = response.amount ?: BigDecimal.ZERO,
                establishmentPacksDTO = response.establishmentPacksDTO ?: emptyList(),
                searchDate = response.searchDate ?: "",
                from = response.from,
                to = response.to,
                bookingAnnulationDTOSet = response.bookingAnnulationDTOSet ?: emptySet(),
                payFromAvoir = response.payFromAvoir ?: false,
                start = response.start?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
                end = response.end,
                amountfeeTrans = response.amountfeeTrans ?: BigDecimal.ZERO,
                currencyId = response.currencyId ?: 0L,
                plannings = response.plannings ?: emptyList(),
                aamount = response.aamount ?: BigDecimal.ZERO,
                numberOfPart =  response.numberOfPart ?: 0,
                sharedExtrasIds = response.sharedExtrasIds ?: emptyList(),
                userIds = response.userIds ?: emptyList(),
                privateExtrasIds = response.privateExtrasIds ?: emptyList(),

                reductionSecondAmount = response.reductionSecondAmount ?: BigDecimal.ZERO,
                reductionaSecondAmount = response.reductionaSecondAmount ?: BigDecimal.ZERO,
                rsamountfeeTrans = response.rsamountfeeTrans ?: BigDecimal.ZERO,
                samountfeeTrans = response.samountfeeTrans ?: BigDecimal.ZERO,
                secondAamount = response.secondAamount ?: BigDecimal.ZERO,
                withSecondPrice = response.withSecondPrice ,
                secondReduction = response.secondReduction ,
                secondAmount = response.secondAmount ,

            )
        }
    }
}
