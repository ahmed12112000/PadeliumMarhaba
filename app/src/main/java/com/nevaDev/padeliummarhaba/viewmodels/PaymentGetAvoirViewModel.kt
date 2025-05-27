package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.usecases.PaymentGetAvoirUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentGetAvoirViewModel @Inject constructor(
    private val paymentGetAvoirUseCase: PaymentGetAvoirUseCase
) : ViewModel() {


    suspend fun PaymentGetAvoir(paymentGetAvoirRequest: PaymentGetAvoirRequest, navController: NavController) : Boolean {
        return try {
            val result = paymentGetAvoirUseCase.PaymentGetAvoir(paymentGetAvoirRequest, navController)
            when (result) {
                is DataResult2.Success -> result.data
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}



