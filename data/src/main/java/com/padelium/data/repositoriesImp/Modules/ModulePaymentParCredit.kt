package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.PaymentParCreditRepositoryImp
import com.padelium.domain.repositories.IPaymentParCreditRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule34 {

    @Binds
    @Singleton
    abstract fun bindPaymentParCreditRepository(
        paymentParCreditRepositoryImp: PaymentParCreditRepositoryImp
    ): IPaymentParCreditRepository
}

