package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PartnerPayResponse
import com.padelium.domain.usecases.GetProfileByIdUseCase
import com.padelium.domain.usecases.PartnerPayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PartnerPayViewModel @Inject constructor(
    private val partnerPayUseCase: PartnerPayUseCase,
    private val getProfileByIdUseCase: GetProfileByIdUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    private val _data = MutableLiveData<List<PartnerPayResponse>>()
    val data: LiveData<List<PartnerPayResponse>> = _data
    private val _partnerPayResponse = MutableLiveData<PartnerPayResponse>()
    val partnerPayResponse: LiveData<PartnerPayResponse> get() = _partnerPayResponse
    private val _storedPartnerPayResponse = MutableLiveData<PartnerPayResponse?>()
    val storedPartnerPayResponse: LiveData<PartnerPayResponse?> get() = _storedPartnerPayResponse
    private val _navigateToErrorScreen = MutableLiveData<Boolean>()
    val navigateToErrorScreen: LiveData<Boolean> get() = _navigateToErrorScreen

    fun partnerPay(userId: Long) {
        dataResult.value = DataResult.Loading

        viewModelScope.launch {
            try {
                val profileResponse = getProfileByIdUseCase.execute(userId)
                val partnerId = profileResponse.id
                if (partnerId != null) {
                    val result = partnerPayUseCase.PartnerPay(partnerId)
                    when (result) {
                        is DataResult.Success -> {
                            val response = result.data
                            if (response is PartnerPayResponse) {
                                _partnerPayResponse.postValue(response)
                                dataResult.postValue(DataResult.Success(response))
                            } else {
                                dataResult.postValue(
                                    DataResult.Failure(null, null, "Invalid data structure"
                                    )
                                )
                            }
                        }

                        is DataResult.Failure -> {
                            dataResult.postValue(
                                DataResult.Failure(
                                    result.exception,
                                    result.errorCode,
                                    result.errorMessage
                                )
                            )
                            if (result.errorCode != 200) {
                                _navigateToErrorScreen.postValue(true)
                            }
                        }

                        is DataResult.Loading -> dataResult.postValue(DataResult.Loading)
                    }
                } else {
                    dataResult.postValue(
                        DataResult.Failure(Exception("No valid partner ID found"), null, "No valid partner ID found"
                        )
                    )
                }
            } catch (ex: Exception) {
                dataResult.postValue(
                    DataResult.Failure(
                        ex,
                        null,
                        ex.localizedMessage ?: "An error occurred"
                    )
                )
            }
        }
    }
}



