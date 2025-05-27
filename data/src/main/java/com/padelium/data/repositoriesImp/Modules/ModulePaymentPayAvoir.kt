package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.PaymentPayAvoirrepositoryImp
import com.padelium.domain.repositories.IPaymentPayAvoirrepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule15 {

    @Binds
    @Singleton
    abstract fun bindPaymentPayAvoirrepository(
        caymentPayAvoirrepositoryImp: PaymentPayAvoirrepositoryImp
    ): IPaymentPayAvoirrepository
}