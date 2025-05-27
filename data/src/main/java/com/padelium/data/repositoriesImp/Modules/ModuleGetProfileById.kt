package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.GetProfileByIdRepositoryImp
import com.padelium.domain.repositories.IGetProfileByIdRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule27 {

    @Binds
    @Singleton
    abstract fun bindGetProfileByIdRepository(
        getProfileByIdRepositoryImp: GetProfileByIdRepositoryImp
    ): IGetProfileByIdRepository
}