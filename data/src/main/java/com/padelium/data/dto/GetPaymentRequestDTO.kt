package com.padelium.data.dto


data class GetPaymentRequestDTO(
    val bookingIds: List<Long>,
    val couponIds: Map<Long, Long>,
    val numberOfPart:Int,
    val orderId: String,
    val privateExtrasIds:List<Long>,
    val sharedExtrasIds: List<Long>,
    val userIds: List<Long>
)
