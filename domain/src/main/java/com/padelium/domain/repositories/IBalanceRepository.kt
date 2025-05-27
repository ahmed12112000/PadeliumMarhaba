package com.padelium.domain.repositories

import retrofit2.Response
import java.math.BigDecimal

interface IBalanceRepository {
       suspend fun Balance (Id: Long): Response<BigDecimal>
}

