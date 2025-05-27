package com.padelium.domain.repositories

import retrofit2.Response

interface IGetEmailRepository {
    suspend fun GetEmail (bookingIds:List<Long>): Response<Long>

}