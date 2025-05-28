package com.padelium.domain.dto


import java.math.BigDecimal


data class GetBookingResponse(
    val aamount: BigDecimal?,
    val amount: BigDecimal?,
    val amountfeeTrans: BigDecimal?,
    val bookingAnnulationDTOSet: Set<bookingAnnulationDTOSet?> = emptySet(),
    val end: String?,
    val establishmentDTO: EstablishmentDTO?,
    val from: String?,
    val numberOfPart: Int?,
    val payFromAvoir: Boolean?,
    val searchDate: String?,
    val start: String?,
    val to: String?,
    val userIds: List<Long?>,
    val plannings: List<PlanningDTO>,
    val privateExtrasIds: List<Long?>,
    val sharedExtrasIds: List<Long?>,
    val establishmentPacksDTO: List<EstablishmentPacksDTO?>,
    val currencyId: Long?,

    val reductionSecondAmount: BigDecimal?,
    val reductionaSecondAmount: BigDecimal?,
    val rsamountfeeTrans: BigDecimal?,
    val samountfeeTrans: BigDecimal?,
    val secondAamount: BigDecimal?,
    val withSecondPrice: Boolean?,

    val secondAmount: BigDecimal?,
    val secondReduction: BigDecimal?,




    )
