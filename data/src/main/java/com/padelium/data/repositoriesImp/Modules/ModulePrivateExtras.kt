package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.PrivateExtrasRepositoryImp
import com.padelium.domain.repositories.IPrivateExtrasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule23 {

    @Binds
    @Singleton
    abstract fun bindPrivateExtrasRepository(
        privateExtrasRepositoryImp: PrivateExtrasRepositoryImp
    ): IPrivateExtrasRepository
}

