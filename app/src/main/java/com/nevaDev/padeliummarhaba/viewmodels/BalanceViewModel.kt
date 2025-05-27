package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.BalanceUseCase
import com.padelium.domain.usecases.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val balanceUseCase: BalanceUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    val navigationEvent = MutableLiveData<String>()

    /**
     * Start processing balance after fetching the profile
     */
    fun fetchAndBalance() {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                when (val profileResult = getProfileUseCase.execute()) {
                    is DataResultBooking.Success -> {
                        val profile = profileResult.data
                        val userId = profile.id
                        when (val result = balanceUseCase.Balance(userId)) {
                            is DataResult.Success -> {
                                val balance = result.data as? BigDecimal ?: BigDecimal.ZERO
                                dataResult.value = DataResult.Success(balance)
                            }
                            is DataResult.Failure -> {
                                if (result.errorCode != 200) {
                                    navigationEvent.value = "server_error_screen"
                                } else {
                                    dataResult.value = DataResult.Failure(
                                        result.exception,
                                        result.errorCode,
                                        result.errorMessage
                                    )
                                }
                            }
                            else -> {
                                dataResult.value = DataResult.Failure(null, null, "Unexpected result")
                            }
                        }
                    }
                    is DataResultBooking.Failure -> {
                        navigationEvent.value = "server_error_screen"
                    }
                    DataResultBooking.Loading -> TODO()
                }
            } catch (ex: Exception) {
                dataResult.value = DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
            }
        }
    }
}




