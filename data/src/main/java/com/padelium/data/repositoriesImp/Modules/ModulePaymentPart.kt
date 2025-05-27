package com.padelium.data.repositoriesImp.Modules


import com.padelium.data.repositoriesImp.RepositoryImp.PaymentPartRepositoryImp
import com.padelium.domain.repositories.IPaymentPartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule35 {

    @Binds
    @Singleton
    abstract fun bindPaymentPartRepository(
        paymentPartRepositoryImp: PaymentPartRepositoryImp
    ): IPaymentPartRepository
}

