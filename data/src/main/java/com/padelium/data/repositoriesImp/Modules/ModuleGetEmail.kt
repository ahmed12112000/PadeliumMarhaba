package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.GetEmailRepositoryImp
import com.padelium.domain.repositories.IGetEmailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule21 {

    @Binds
    @Singleton
    abstract fun bindGetEmailrepository(
        getEmailRepositoryImp: GetEmailRepositoryImp
    ): IGetEmailRepository
}

