package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.CreditPayResponse
import com.padelium.domain.repositories.ICreditPayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CreditPayViewModel @Inject constructor(
    private val repository: ICreditPayRepository
) : ViewModel() {

    private val _CreditsData = MutableLiveData<DataResultBooking<List<CreditPayResponse>>>()
    val CreditsData: LiveData<DataResultBooking<List<CreditPayResponse>>> get() = _CreditsData

    // Add a MutableLiveData to handle navigation events
    val navigationEvent = MutableLiveData<String>()

    fun GetCreditPay() {
        _CreditsData.postValue(DataResultBooking.Loading)

        viewModelScope.launch {
            try {
                val response = repository.GetCreditPay()
                Log.d("CreditsData", "Fetched Credits: $response")

                if (response.isNotEmpty()) {
                    _CreditsData.postValue(DataResultBooking.Success(response))
                } else {
                    _CreditsData.postValue(
                        DataResultBooking.Failure(
                            exception = null,
                            errorCode = null,
                            errorMessage = "No reservations found."
                        )
                    )
                }
            } catch (e: Exception) {
                // Handle exception and post failure data
                _CreditsData.postValue(
                    DataResultBooking.Failure(
                        exception = e,
                        errorCode = null,
                        errorMessage = "Exception occurred: ${e.message}"
                    )
                )
                Log.e("CreditsData", "Error fetching Credits", e)
            }

            // Check if the errorCode is not 200 and navigate to the error screen
            when (val result = _CreditsData.value) {
                is DataResultBooking.Failure -> {
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                    // No action required for other cases
                }
            }
        }
    }
}


