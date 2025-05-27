package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.usecases.PaymentAvoirUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserAvoirViewModel @Inject constructor(private val paymentAvoirUseCase: PaymentAvoirUseCase) : ViewModel()
{

    val dataResult = MutableLiveData<DataResult>()

    fun PaymentAvoir(userAvoirRequest: UserAvoirRequest) {

        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                val result = paymentAvoirUseCase.PaymentAvoir(userAvoirRequest)
                if (result is DataResult.Success && result.data == null) {
                }
                dataResult.value = result
            } catch (e: Exception) {
                dataResult.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "Payment failed: ${e.message}"
                )
            }
        }
    }
}

