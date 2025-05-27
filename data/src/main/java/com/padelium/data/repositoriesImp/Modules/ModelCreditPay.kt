package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.CreditPayRepositoryImp
import com.padelium.domain.repositories.ICreditPayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule13 {

    @Binds
    @Singleton
    abstract fun bindcreditRepository(
        creditRepositoryImp: CreditPayRepositoryImp
    ): ICreditPayRepository
}
