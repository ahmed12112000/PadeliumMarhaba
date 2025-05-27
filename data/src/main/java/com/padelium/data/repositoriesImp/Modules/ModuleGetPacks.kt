package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.GetPacksRepositoryImp
import com.padelium.domain.repositories.IGetPacksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule11 {

    @Binds
    @Singleton
    abstract fun bindGetBookingRepository(
        getPacksRepositoryImp: GetPacksRepositoryImp
    ): IGetPacksRepository
}