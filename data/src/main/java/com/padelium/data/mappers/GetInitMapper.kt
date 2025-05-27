package com.padelium.data.mappers

import com.padelium.data.dto.GetInitResponseDTO
import com.padelium.domain.dto.GetInitResponse
import javax.inject.Inject

class GetInitMapper  @Inject constructor() {
    fun GetInitResponseToGetInitResponseDto(getInitResponse: GetInitResponse): GetInitResponseDTO {
        return GetInitResponseDTO(
            key = getInitResponse.key,
            dateTime = getInitResponse.dateTime,
            activityId = getInitResponse.activityId,
            cityId = getInitResponse.cityId,
            activityName = getInitResponse.activityName,
            establishmentId = getInitResponse.establishmentId,
            cityName = getInitResponse.cityName,
            isCity = getInitResponse.isCity,
            time = getInitResponse.time

        )
    }
}