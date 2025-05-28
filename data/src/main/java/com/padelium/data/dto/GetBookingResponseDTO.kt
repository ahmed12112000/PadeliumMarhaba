package com.padelium.data.dto


import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.bookingAnnulationDTOSet
import java.math.BigDecimal
import java.lang.reflect.Type


data class GetBookingResponseDTO(
    @JsonAdapter(EstablishmentDTOAdapter::class)
    val aamount: BigDecimal?,
    val amount: BigDecimal?,
    val amountfeeTrans: BigDecimal?,
    val bookingAnnulationDTOSet: Set<bookingAnnulationDTOSet?> = emptySet(),
    val end: String?,
    val establishmentDTO: EstablishmentDTO?,
    val from: String?,
    val numberOfPart: Int?,
    val payFromAvoir: Boolean?,
    val searchDate: String?,
    val start: String?,
    val to: String?,
    val userIds: List<Long?>,
    val plannings: List<PlanningDTO>,
    val privateExtrasIds: List<Long?>,
    val sharedExtrasIds: List<Long?>,
    val establishmentPacksDTO: List<EstablishmentPacksDTO?>,
    val currencyId: Long?,

    val reductionSecondAmount: BigDecimal?,
    val reductionaSecondAmount: BigDecimal?,
    val rsamountfeeTrans: BigDecimal?,
    val samountfeeTrans: BigDecimal?,
    val secondAamount: BigDecimal?,
    val withSecondPrice: Boolean?,

    val secondAmount: BigDecimal?,
    val secondReduction: BigDecimal?,

    )

class EstablishmentDTOAdapter : JsonDeserializer<EstablishmentDTO?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): EstablishmentDTO? {
        return if (json.isJsonObject) {
            context.deserialize(json, EstablishmentDTO::class.java)
        } else {
            null
        }
    }
}
