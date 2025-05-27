package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.PaymentPayAvoirUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class PaymentPayAvoirViewModel @Inject constructor(
    private val paymentPayAvoirUseCase: PaymentPayAvoirUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start processing payment
     */
    fun PaymentPayAvoir(amount: BigDecimal) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = paymentPayAvoirUseCase.PaymentPayAvoir(amount)
            dataResult.value = result
            when (result) {
                is DataResult.Success ->{}
                is DataResult.Failure -> {}
                else -> {}
            }
        }
    }
}

