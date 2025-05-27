package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.usecases.PaymentPartBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PaymentPartBookingViewModel @Inject constructor(private val paymentPartBookingUseCase: PaymentPartBookingUseCase):
    ViewModel() {


    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start getting data
     */
    suspend fun PaymentPartBooking(
        paymentPartBookingRequest: PaymentPartBookingRequest,
        navController: NavController
    ): Boolean {
        return try {
            val result = paymentPartBookingUseCase.PaymentPartBooking(
                paymentPartBookingRequest,
                navController
            )
            when (result) {
                is DataResult2.Success -> result.data
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}
