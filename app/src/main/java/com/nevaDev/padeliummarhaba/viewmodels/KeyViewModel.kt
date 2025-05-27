package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.FetchKeyResponseDTO
import com.padelium.data.mappers.KeyMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.usecase.KeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class KeyViewModel @Inject constructor(
    private val keyUseCase: KeyUseCase,
    private val keyMapper: KeyMapper,
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<FetchKeyResponseDTO>>()
    val navigateToErrorScreen = MutableLiveData<Boolean>()

    fun getReservationKey(fetchKeyRequest: FetchKeyRequest, selectedDate: LocalDate) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = keyUseCase.getReservationKey(fetchKeyRequest)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val fetchKeyResponseDTO = keyMapper.fetchKeyResponseToFetchKeyResponseDTO(result.data)
                    DataResultBooking.Success(fetchKeyResponseDTO)
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
