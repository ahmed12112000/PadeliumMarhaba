package com.padelium.domain.repositories

import com.padelium.domain.dto.ExtrasResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal


interface IExtrasRepository {
    suspend fun Extras(): List<ExtrasResponse>
}

