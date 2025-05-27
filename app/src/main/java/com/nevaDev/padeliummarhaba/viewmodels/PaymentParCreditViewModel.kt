package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.usecases.PaymentParCreditUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentParCreditViewModel @Inject constructor(private val paymentParCreditUseCase: PaymentParCreditUseCase):
    ViewModel()
{


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun PaymentParCredit(paymentParCreditRequest: PaymentParCreditRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value=paymentParCreditUseCase.PaymentParCredit(paymentParCreditRequest)
        }
    }

}

