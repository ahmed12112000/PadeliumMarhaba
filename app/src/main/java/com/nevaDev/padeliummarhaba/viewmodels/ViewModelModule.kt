package com.nevaDev.padeliummarhaba.viewmodels

import com.padelium.data.mappers.GetBookingMapper
import com.padelium.data.mappers.GetInitMapper
import com.padelium.data.mappers.KeyMapper
import com.padelium.data.mappers.SearchListMapper
import com.padelium.domain.usecase.KeyUseCase
import com.padelium.domain.usecases.GetBookingUseCase
import com.padelium.domain.usecases.GetInitUseCase
import com.padelium.domain.usecases.InitBookingUseCase
import com.padelium.domain.usecases.SearchListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideGetBookingViewModel(
        getBookingUseCase: GetBookingUseCase,
        getBookingMapper: GetBookingMapper
    ): GetBookingViewModel {
        return GetBookingViewModel(getBookingUseCase, getBookingMapper)
    }

    @Provides
    fun provideInitBookingViewModel(
        initBookingUseCase: InitBookingUseCase,
    ): InitBookingViewModel {
        return InitBookingViewModel(initBookingUseCase)
    }

    @Provides
    fun provideSearchListViewModel(
        searchListUseCase: SearchListUseCase,
        searchListMapper: SearchListMapper,
    ): SearchListViewModel {
        return SearchListViewModel(searchListUseCase, searchListMapper)
    }

@Provides
fun provideGetInitViewModel(
    getInitUseCase: GetInitUseCase,
    getInitMapper: GetInitMapper,
): GetInitViewModel {
    return GetInitViewModel(getInitUseCase, getInitMapper)
}
    @Provides
    fun provideKeyViewModel(
        keyUseCase: KeyUseCase,
        keyMapper: KeyMapper,

    ): KeyViewModel {
        return KeyViewModel(
            keyUseCase,
            keyMapper,
        )
    }
}