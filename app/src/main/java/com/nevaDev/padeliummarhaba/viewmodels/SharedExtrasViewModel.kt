package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.SharedExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedExtrasViewModel @Inject constructor(private val sharedExtrasUseCase: SharedExtrasUseCase) : ViewModel()
{

    private val _extrasState = MutableLiveData<DataResult>()
    val extrasState1: LiveData<DataResult> get() = _extrasState

    fun SharedExtras() {
        viewModelScope.launch {
            _extrasState.postValue(DataResult.Loading)

            val result = sharedExtrasUseCase.SharedExtras()
            _extrasState.postValue(result)
        }
    }
    fun calculateAdjustedSharedExtras(totalSharedExtrasCost: Double, selectedParts: Int): Double {
        return when (selectedParts) {
            1 -> totalSharedExtrasCost / 4
            2 -> totalSharedExtrasCost / 2
            3 -> (totalSharedExtrasCost / 4) * 3
            4 -> totalSharedExtrasCost
            else -> totalSharedExtrasCost
        }
    }
}

