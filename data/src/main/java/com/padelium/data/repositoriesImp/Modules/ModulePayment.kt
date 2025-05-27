package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.PaymentRepositoryImp
import com.padelium.domain.repositories.IPaymentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule5 {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImp: PaymentRepositoryImp
    ): IPaymentRepository
}
