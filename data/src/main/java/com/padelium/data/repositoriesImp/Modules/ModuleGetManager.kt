package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.GetManagerRepositoryImp
import com.padelium.domain.repositories.IGetManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule20 {

    @Binds
    @Singleton
    abstract fun bindGetManagerrepository(
        getManagerRepositoryImp: GetManagerRepositoryImp
    ): IGetManagerRepository
}

