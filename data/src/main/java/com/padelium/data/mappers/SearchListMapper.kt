package com.padelium.data.mappers

import com.padelium.data.dto.SearchListResponseDTO
import com.padelium.domain.dto.SearchListResponse
import javax.inject.Inject

class SearchListMapper @Inject constructor() {

    fun SearchListResponseToSearchListResponseDto(searchListResponse: List<SearchListResponse>): List<SearchListResponseDTO> { return searchListResponse.map { response ->
            SearchListResponseDTO(
                id = response.id,
                name = response.name?: "",
                code = response.code?: "",
                description = response.description?: "",
                email = response.email?: "",
                address = response.address?: "",
                latitude = response.latitude,
                longitude = response.longitude,
                cityName = response.cityName?: "",
                activityName = response.activityName?: "",
                facadeUrl = response.facadeUrl?: "",
                amount = response.amount,
                currencyName = response.currencyName?: "",
                decimalNumber = response.decimalNumber,
                currencySymbol = response.currencySymbol?: "",
                totalFeed = response.totalFeed,
                moyFeed = response.moyFeed,
                secondAmount = response.secondAmount,
                numberOfPlayer = response.numberOfPlayer,
                openTime = response.openTime?: "",
                closedTime = response.closedTime?: "",
                reductionAmount = response.reductionAmount,
                reductionSecondAmount = response.reductionSecondAmount,
                establishmentPictureDTO = response.establishmentPictureDTO?: emptyList(),
                logo = response.logo?: "",
                key = response.key?: "",
                timeSpan = response.timeSpan,
                createdDate = response.createdDate?: "",
                establishmentId = response.establishmentId,
                client = response.client,
                fromStr = response.fromStr?: "",
                toStr = response.toStr?: "",
                plannings = response.plannings ?: emptyList(),
                )
        } }
}

