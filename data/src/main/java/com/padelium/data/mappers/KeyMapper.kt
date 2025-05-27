package com.padelium.data.mappers

import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.FetchKeyResponseDTO
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class KeyMapper @Inject constructor(){
    fun FetchKeyResponseToFetchKeyResponseDTO(fetchKeyRequest: FetchKeyRequest): FetchKeyRequestDTO {

        val formattedDateTime = fetchKeyRequest.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return FetchKeyRequestDTO(dateTime = formattedDateTime)
    }
    fun fetchKeyResponseToFetchKeyResponseDTO(fetchKeyResponse: FetchKeyResponse): FetchKeyResponseDTO {
        return FetchKeyResponseDTO(
            key = fetchKeyResponse.key,
            dateTime = fetchKeyResponse.dateTime,
            activityId = fetchKeyResponse.activityId,
            cityId = fetchKeyResponse.cityId,
            activityName = fetchKeyResponse.activityName,
            establishmentId = fetchKeyResponse.establishmentId,
            cityName = fetchKeyResponse.cityName,
            isCity = fetchKeyResponse.isCity,
            time = fetchKeyResponse.time

        )
    }
}


