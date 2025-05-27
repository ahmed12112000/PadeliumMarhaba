package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.PrivateExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrivateExtrasViewModel @Inject constructor(private val privateExtrasUseCase: PrivateExtrasUseCase) : ViewModel()
{

    private val _extrasState = MutableLiveData<DataResult>()
    val extrasState2: LiveData<DataResult> get() = _extrasState

    fun PrivateExtras() {
        viewModelScope.launch {
            _extrasState.postValue(DataResult.Loading)

            val result = privateExtrasUseCase.PrivateExtras()
            _extrasState.postValue(result)
        }
    }
}
