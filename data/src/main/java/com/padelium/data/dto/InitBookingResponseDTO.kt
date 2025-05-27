package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.PlanningDTOo
import com.padelium.domain.dto.bookingAnnulationDTOSet
import com.padelium.domain.dto.bookingDTO
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class InitBookingResponseDTO(

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
    val happyHours: String,
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
    val Client:Boolean,
    val secondReduction: BigDecimal,
    val aamount: BigDecimal,


    ) : Serializable
