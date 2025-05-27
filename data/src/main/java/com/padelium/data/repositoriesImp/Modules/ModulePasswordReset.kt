package com.padelium.data.repositoriesImp.Modules


import com.padelium.data.repositoriesImp.RepositoryImp.DeleteAccountRepositoryImp
import com.padelium.data.repositoriesImp.RepositoryImp.GetPasswordRepositoryImp
import com.padelium.data.repositoriesImp.RepositoryImp.ResetPasswordRepositoryImp
import com.padelium.domain.repositories.IDeleteAccountRepository
import com.padelium.domain.repositories.IGetPasswordRepository
import com.padelium.domain.repositories.IResetPasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule40 {

    @Binds
    @Singleton
    abstract fun bindGetPasswordrepository(
        getPasswordRepositoryImp: GetPasswordRepositoryImp
    ): IGetPasswordRepository
}


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule41 {

    @Binds
    @Singleton
    abstract fun bindResetPasswordrepository(
        resetPasswordRepositoryImp: ResetPasswordRepositoryImp
    ): IResetPasswordRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule42 {

    @Binds
    @Singleton
    abstract fun bindDeleteAccountrepository(
        DeleteAccountRepositoryImp: DeleteAccountRepositoryImp
    ): IDeleteAccountRepository
}

