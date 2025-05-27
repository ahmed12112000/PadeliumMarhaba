package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.SharedExtrasRepositoryImp
import com.padelium.domain.repositories.ISharedExtrasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule24 {

    @Binds
    @Singleton
    abstract fun bindSharedExtrasRepository(
        sharedExtrasRepositoryImp: SharedExtrasRepositoryImp
    ): ISharedExtrasRepository
}

