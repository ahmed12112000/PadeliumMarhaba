package com.padelium.data.mappers

import com.padelium.data.dto.GetStatusesResponseDTO
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.GetStatusesResponse
import javax.inject.Inject


class GetStatusesMapper @Inject constructor() {
    fun mapGetStatusesResponseDTOToGetGetStatusesResponse(getStatusesList: List<GetStatusesResponseDTO>): List<GetStatusesResponse> {
        return getStatusesList.map { response ->
            GetStatusesResponse(
                id = response.id ?: 0,
                 name = response.name?.toString() ?: "",
                  code = response.code?.toString() ?: "",
                   created = response.created?.toString() ?: "",
                    updated = response.updated?.toString() ?: "",
                    isshow = response.isshow ?: false,
            )
        }
    }
}

