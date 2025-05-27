package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.usecases.GetPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GetPaymentViewModel @Inject constructor(
    private val getPaymentUseCase: GetPaymentUseCase
) : ViewModel() {

    suspend fun GetPayment2(getPaymentRequest: GetPaymentRequest, navController: NavController): Boolean {
        return try {
            val result = getPaymentUseCase.GetPayment(getPaymentRequest, navController)
            when (result) {
                is DataResult2.Success -> result.data
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}
