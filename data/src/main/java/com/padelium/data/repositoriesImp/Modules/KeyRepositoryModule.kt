package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.KeyRepositoryImp
import com.padelium.domain.repositories.IGetKeyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Use SingletonComponent if the repository should be app-wide
abstract class KeyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindKeyRepository(
        keyRepositoryImpl: KeyRepositoryImp
    ): IGetKeyRepository
}