package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.usecases.ConfirmBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConfirmBookingViewModel @Inject constructor(
    private val confirmBookingUseCase: ConfirmBookingUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    val navigationEvent = MutableLiveData<String>()

    /**
     * Start getting data
     */
    fun GetPayment(confirmBookingRequest: ConfirmBookingRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = confirmBookingUseCase.ConfirmBooking(confirmBookingRequest)
            when (result) {
                is DataResult.Success -> {
                    dataResult.value = result
                }
                is DataResult.Failure -> {
                    dataResult.value = result // Handle failure
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                    dataResult.value = DataResult.Failure(null, null, "Unexpected result")
                }
            }
        }
    }
}

