package com.padelium.data.mappers

import com.padelium.data.dto.GetPacksResponseDTO
import com.padelium.domain.dto.GetPacksResponse
import javax.inject.Inject


class GetPacksMapper @Inject constructor() {
    fun GetPacksResponseToGetPacksResponseDTO(getPacksResponseList: List<GetPacksResponseDTO>): List<GetPacksResponse> {
        return getPacksResponseList.map { response ->
            GetPacksResponse(
                id = response.id,
                name = response.name,
                description = response.description,
                code = response.code,
                amount = response.amount,
                isonline = response.isonline,
                title = response.title,
                currency = response.currency
            )
        }
    }
}


