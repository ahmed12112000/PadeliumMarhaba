package com.padelium.data.repositoriesImp




import com.padelium.domain.repositories.IFindTermsRepository
import com.padelium.domain.repositories.IUpdatePhoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule26 {

    @Binds
    @Singleton
    abstract fun bindUpdatePhoneRepository(
        updatePhoneRepositoryImp: UpdatePhoneRepositoryImp
    ): IUpdatePhoneRepository
}
