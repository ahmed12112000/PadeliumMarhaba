package com.padelium.data.dto

import com.padelium.domain.dto.bookingExtrasDTO
import com.padelium.domain.dto.bookingUsersPaymentDTO
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

data class GetReservationResponseDTO(


    val id: Long,
    val from: String,
    val to: String,
    val annulationDate: String?, // Changed to nullable
    val sellAmount: Double,
    val purchaseAmount: Double,
    val numberOfPlayer: Int?,
    val reference: String,
    val description: String?,
    val isRefundable: Boolean?,
    val created: String,
    val updated: String,
    val createdBy: Long?,
    val updatedBy: Long?,
    val currencyFromId: Long,
    val currencyToId: Long,
    val bookingStatusId: Long,
    val establishmentId: Long,
    val userId: Long,
    val userLogin: String,
    val establishmentName: String,
    val bookingStatusName: String,
    val userEmail: String,
    val currencyFromSymbol: String,
    val gainFromManager: Double?,
    val bookingStatusCode: String,
    val userPhone: String,
    val cancelBook: Boolean,
    val cancel: Boolean,
    val isonline: Boolean,
    val activityName: String,
    val cityName: String,
    val establishmentCode: String,
    val localAmount: Double,
    val reduction: Int?,
    val showcancel: Boolean, // Note: JSON has "showCancel" - you might need @SerializedName
    val showfeedBack: Boolean,
    val bookingDate: String,
    val token: String?,
    val paymentError: Boolean,
    val paymentprog: Boolean,
    val amountToPay: Double?,
    val sobflousCode: String?,
    val couponId: Long?,
    val isCoupon: Boolean?,
    val couponValue: String?,
    val couponCode: String?,
    val establishmentPacksId: Long?,
    val establishmentTypeCode: String,
    val isConfirmed: Boolean,
    val isFromEvent: Boolean,
    val establishmentPacksFirstTitle: String?,
    val establishmentPacksSecondTitle: String?,
    val usersIds: List<Long>?,
    val fromStr: String,
    val toStr: String,
    val fromStrTime: String,
    val toStrTime: String,
    val activePayment: Boolean = false,
    val isWaitForPay: Boolean = false,
    val bookingLabelId: Long?,
    val bookingLabelName: String?,
    val bookingLabelColor: String?, // Changed from bookingLabelColors to match JSON
    val sharedExtrasIds: List<Long>?,
    val privateExtrasIds: List<Long>?,
    val privateExtrasLocalIds: MutableMap<Long, List<Long>>? = null, // Made nullable
    val userFirstName: String,
    val userLastName: String,
    val extras: List<bookingExtrasDTO>?,
    val numberOfPart: Int?,
    val createdStr: String,

    ) : Serializable
