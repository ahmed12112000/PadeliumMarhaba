package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.GetStatusesRepositoryImp
import com.padelium.domain.repositories.IGetStatusesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule28 {

    @Binds
    @Singleton
    abstract fun bindGetstatusRepository(
        getStatusesRepositoryImp: GetStatusesRepositoryImp
    ): IGetStatusesRepository
}