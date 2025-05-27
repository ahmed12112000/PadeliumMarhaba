package com.nevaDev.padeliummarhaba.di

import android.content.Context
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.datasource.remote.PadeliumEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PadeliumApiModule {

    @Provides
    fun providePadeliumApi(
        padeliumEndPoint: PadeliumEndPoint,
        @ApplicationContext context: Context
    ): PadeliumApi {
        return PadeliumApi(padeliumEndPoint, context)
    }
}

