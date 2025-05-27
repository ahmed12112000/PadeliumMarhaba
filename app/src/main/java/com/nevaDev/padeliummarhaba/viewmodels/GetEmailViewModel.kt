package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.GetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetEmailViewModel @Inject constructor(
    private val getEmailUseCase: GetEmailUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    val navigationEvent = MutableLiveData<String>()

    /**
     * Start processing payment
     */
    fun GetEmail(bookingIds: List<Long>) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = getEmailUseCase.GetEmail(bookingIds)
            dataResult.value = result
            when (result) {
                is DataResult.Success -> {
                }
                is DataResult.Failure -> {

                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                }
            }
        }
    }
}



