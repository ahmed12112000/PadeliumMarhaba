package com.padelium.data.repositoriesImp.Modules

import com.padelium.data.repositoriesImp.RepositoryImp.BalanceRepositoryImp
import com.padelium.domain.repositories.IBalanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule16 {

    @Binds
    @Singleton
    abstract fun bindBalancerepository(
        balanceRepositoryImp: BalanceRepositoryImp
    ): IBalanceRepository
}
