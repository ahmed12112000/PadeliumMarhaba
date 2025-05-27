package com.padelium.data.repositoriesImp.Modules



import com.padelium.data.repositoriesImp.RepositoryImp.PaymentGetAvoirRepositoryImp
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule22 {

    @Binds
    @Singleton
    abstract fun bindPaymentGetAvoirRepository(
        paymentGetAvoirRepositoryImp: PaymentGetAvoirRepositoryImp
    ): IPaymentGetAvoirRepository
}
