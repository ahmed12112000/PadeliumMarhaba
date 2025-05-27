package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.ExtrasRepositoryImp
import com.padelium.domain.repositories.IExtrasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule8 {

    @Binds
    @Singleton
    abstract fun bindExtrasRepository(
        extrasRepositoryImp: ExtrasRepositoryImp
    ): IExtrasRepository
}

