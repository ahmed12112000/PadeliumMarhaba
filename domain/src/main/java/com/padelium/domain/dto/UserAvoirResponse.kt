package com.padelium.domain.dto

data class UserAvoirResponse(
    val payUrl: String?,
    val paymentRef: String?,
    val orderId: String,
    val formUrl: String,
    val errorCode: String?,
    val errorMessage: String?,
    val orderStatus: String?,


)





