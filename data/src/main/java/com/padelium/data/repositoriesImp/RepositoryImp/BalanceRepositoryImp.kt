package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IBalanceRepository
import retrofit2.Response
import java.math.BigDecimal
import javax.inject.Inject


class BalanceRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IBalanceRepository {

    override suspend fun Balance(Id: Long): Response<BigDecimal> {
        return api.Balance(Id)
    }
}
