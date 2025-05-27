package com.padelium.domain.dto

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class InitBookingResponse(
    val establishmentDTO: EstablishmentDTO,
    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val amount: BigDecimal,
    val decimalNumber: Int,
    val currencySymbol: String,
    val facadeUrl: String,
    val openTime: Instant,
    val closeTime: Instant,
    val searchDate: String,
    val from: String,
    val to: String,
    val numberOfPlayer: Int,
    val description: String,
    val currencyId: Long,
    val mgAmount: BigDecimal,
    val totalFeed: Int,
    val moyFeed: Double,
    val bookingAnnulationDTOSet: List<bookingAnnulationDTOSet>,
    val secondAmount: BigDecimal,
    val secondAamount: BigDecimal,
    val happyHours: List<HappyHourss>?,
    val withSecondPrice: Boolean,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val payFromAvoir: Boolean,
    val reduction: BigDecimal,
    val reductionaAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val start: String,
    val end: String,
    val amountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val ramountfeeTrans: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val couponCode: String,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val plannings: List<PlanningDTOo>,
    val users: List<Long>,
    val client:Boolean,
    val secondReduction: BigDecimal,
    val aamount: BigDecimal,

    ) : Serializable
