package com.nevaDev.padeliummarhaba.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.usecases.ErrorCreditUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ErrorCreditViewModel @Inject constructor(
    private val errorCreditUseCase: ErrorCreditUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    private val _dataResult1 = MutableLiveData<Resulta>()

    fun ErrorCredit(creditErrorRequest: CreditErrorRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value = errorCreditUseCase.ErrorCredit(creditErrorRequest)
        }
    }

}
