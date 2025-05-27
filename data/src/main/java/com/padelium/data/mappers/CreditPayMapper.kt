package com.padelium.data.mappers

import com.padelium.data.dto.CreditPayResponseDTO
import com.padelium.domain.dto.CreditPayResponse
import java.math.BigDecimal
import javax.inject.Inject


class CreditPayMapper @Inject constructor() {
    fun CreditPayResponseToCreditPayResponseDTO(creditPayResponseList: List<CreditPayResponseDTO>): List<CreditPayResponse> {
        return creditPayResponseList.map { response ->
            CreditPayResponse(
                id = response.id,
                amount = response.amount?: BigDecimal.ZERO,
                created = response.created ?: "",
                updated = response.updated ?: "",
                createdBy = response.createdBy,
                updatedBy = response.updatedBy ?: 0,
                description = response.description ?: "",
                userId = response.userId,
                userLogin = response.userLogin ?: "",
                userFirstName = response.userFirstName ?: "",
                userLastName = response.userLastName ?: "",
                bookingId = response.bookingId ?: 0,
                bookingReference = response.bookingReference ?: "",
                bookingSellAmount = response.bookingSellAmount ?: "",
                bookingCreation = response.bookingCreation ?: "",
                bookingEstablishmentName = response.bookingEstablishmentName ?: "",
                createdByFirstName = response.createdByFirstName ?: "",
                createdByLastName = response.createdByLastName ?: "",
                createdByLogin = response.createdByLogin ?: "",
                token = response.token ?: "",
                transactionId = response.transactionId ?: "",
                userAvoirTypeId = response.userAvoirTypeId,
                userAvoirTypeName = response.userAvoirTypeName ?: "",
                createdStr = response.createdStr ?: "",
                buyerId = response.buyerId ?: "",
                packId = response.packId ?: "",
                bookingEstablishmentCode = response.bookingEstablishmentCode ?: ""
            )
        }
    }
}
