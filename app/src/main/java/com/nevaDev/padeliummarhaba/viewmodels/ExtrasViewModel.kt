package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.ExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtrasViewModel @Inject constructor(private val extrasUseCase: ExtrasUseCase) : ViewModel() {

    private val _extrasState = MutableLiveData<DataResult>()
    val extrasState: LiveData<DataResult> get() = _extrasState
    val navigationEvent = MutableLiveData<String>()

    fun Extras() {
        viewModelScope.launch {
            _extrasState.postValue(DataResult.Loading)

            val result = extrasUseCase.Extras()
            _extrasState.postValue(result)

            when (result) {
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
