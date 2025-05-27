package com.essid.orangebanque.di

import com.padelium.data.repositoriesImp.RepositoryImp.UserRepositoryImp
import com.padelium.domain.repositories.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(reposRepositoryImp: UserRepositoryImp): IUserRepository
}
