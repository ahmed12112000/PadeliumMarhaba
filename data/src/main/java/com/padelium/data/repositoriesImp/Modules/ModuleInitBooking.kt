package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.InitBookingRepositorylmp
import com.padelium.domain.repositories.IInitBookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule2 {

    @Binds
    @Singleton
    abstract fun bindInitBookingRepository(
        initBookingRepositorylmp: InitBookingRepositorylmp
    ): IInitBookingRepository
}