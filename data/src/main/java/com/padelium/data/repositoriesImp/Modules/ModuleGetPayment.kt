package com.padelium.data.repositoriesImp.Modules


import com.padelium.data.repositoriesImp.RepositoryImp.GetPaymentRepositoryImp
import com.padelium.domain.repositories.IGetPaymentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule6 {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        getPaymentRepositoryImp: GetPaymentRepositoryImp
    ): IGetPaymentRepository
}

