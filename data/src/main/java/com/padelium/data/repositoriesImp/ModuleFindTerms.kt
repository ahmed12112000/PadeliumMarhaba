package com.padelium.data.repositoriesImp


import com.padelium.domain.repositories.IFindTermsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule25 {

    @Binds
    @Singleton
    abstract fun bindFindTermsRepository(
        findTermsRepositoryImp: FindTermsRepositoryImp
    ): IFindTermsRepository
}


