package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IPaymentPayAvoirrepository
import retrofit2.Response
import java.math.BigDecimal
import javax.inject.Inject


class PaymentPayAvoirrepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IPaymentPayAvoirrepository {
    override suspend fun PaymentPayAvoir(amount: BigDecimal): Response<Boolean> {
        return api.PaymentPayAvoir(amount)
    }
}
