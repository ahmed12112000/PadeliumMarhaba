package com.padelium.data.repositoriesImp.Modules


import com.padelium.data.repositoriesImp.RepositoryImp.ConfirmBookingRepositoryImp
import com.padelium.domain.repositories.IConfirmBookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule7 {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        confirmBookingRepositoryImp: ConfirmBookingRepositoryImp
    ): IConfirmBookingRepository
}
