package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.PlanningDTO

data class SearchListResponseDTO(
    val id: Long,
    val name: String,
    val code: String,
    val description: String?,
    val email: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val activityName: String,
    val facadeUrl: String,
    val amount: Double,
    val currencyName: String,
    val decimalNumber: Int,
    val currencySymbol: String,
    val totalFeed: Int,
    val moyFeed: Double,
    val secondAmount: Double?,
    val numberOfPlayer: Int,
    val openTime: String?,
    val closedTime: String?,
    val reductionAmount: Double?,
    val reductionSecondAmount: Double?,
    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val logo: String?,
    val key: String?,
    val timeSpan: Int,
    val createdDate: String,
    val establishmentId: Long,
    val client: Boolean,
    val fromStr: String,
    val toStr: String,
    val plannings: List<PlanningDTO>,

    )

