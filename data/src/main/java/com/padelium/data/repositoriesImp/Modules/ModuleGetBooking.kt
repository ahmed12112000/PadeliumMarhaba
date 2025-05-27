package com.padelium.data.repositoriesImp.Modules
import com.padelium.data.repositoriesImp.RepositoryImp.GetBookingRepositoryImp
import com.padelium.domain.repositories.IGetBookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule3 {

    @Binds
    @Singleton
    abstract fun bindGetBookingRepository(
        getBookingRRepositoryImp: GetBookingRepositoryImp
    ): IGetBookingRepository
}
