package com.padelium.data.mappers


import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.data.dto.PrivateExtrasResponseDTO
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.dto.PrivateExtrasResponse
import javax.inject.Inject


class PrivateExtrasMapper @Inject constructor() {

    fun PrivateExtrasRequestToPrivateExtrasResponseDTO(privateextrasResponse: List<PrivateExtrasResponseDTO>): List<PrivateExtrasResponse> { return privateextrasResponse.map { response ->
            PrivateExtrasResponse(
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
        } }
}
