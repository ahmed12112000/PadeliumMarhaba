package com.padelium.data.mappers


import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.domain.dto.ExtrasResponse
import javax.inject.Inject


class ExtrasMapper @Inject constructor() {

    fun ExtrasRequestToExtrasResponseDTO(extrasResponse: List<ExtrasResponseDTO>): List<ExtrasResponse> {
        return extrasResponse.map { response ->
            ExtrasResponse(
                id = response.id,
                name = response.name,
                code = response.code,
                description = response.description ?: "",
                picture = response.picture ?: "",
                amount = response.amount,
                currencyName = response.currencyName.toString(),
                isShared = response.isShared,
                currencyId = response.currencyId,
                )
        }
    }
}
