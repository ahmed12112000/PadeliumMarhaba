package com.padelium.data.repositoriesImp.Modules


import com.padelium.data.repositoriesImp.RepositoryImp.PartnerPayRepositoryImp
import com.padelium.domain.repositories.IPartnerPayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule29 {

    @Binds
    @Singleton
    abstract fun bindPartnerPayRepository(
        partnerPayRepositoryImp: PartnerPayRepositoryImp
    ): IPartnerPayRepository
}

