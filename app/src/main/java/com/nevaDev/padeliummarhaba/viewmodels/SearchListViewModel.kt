package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.SearchListResponseDTO
import com.padelium.data.mappers.SearchListMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.SearchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val searchListUseCase: SearchListUseCase,
    private val searchListMapper: SearchListMapper,
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<List<SearchListResponseDTO>>>()
    val navigateToErrorScreen = MutableLiveData<Boolean>()

    fun searchList(key: String) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = searchListUseCase.execute(key)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val searchListResponseDTO = searchListMapper.SearchListResponseToSearchListResponseDto(result.data)
                    DataResultBooking.Success(searchListResponseDTO)
                }

                is DataResultBooking.Failure -> {
                    result.errorCode?.let { errorCode ->
                        if (errorCode != 200) {
                            navigateToErrorScreen.value = true
                        }
                    }
                    DataResultBooking.Failure(
                        exception = result.exception,
                        errorCode = result.errorCode,
                        errorMessage = ""
                    )
                }

                else -> {
                    DataResultBooking.Failure(
                        exception = null,
                        errorCode = null,
                        errorMessage = ""
                    )
                }
            }
        }
    }
}


