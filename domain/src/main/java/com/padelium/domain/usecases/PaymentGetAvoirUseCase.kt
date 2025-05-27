package com.padelium.domain.usecases

import android.util.Log
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import com.padelium.domain.repositories.IPaymentParCreditRepository
import com.padelium.domain.repositories.IPaymentPartBookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentGetAvoirUseCase @Inject constructor(
    private val paymentGetAvoirRepository: IPaymentGetAvoirRepository
) {
    suspend fun PaymentGetAvoir(paymentGetAvoirRequest: PaymentGetAvoirRequest, navController: NavController): DataResult2<Boolean> {
        return try {
            val response = paymentGetAvoirRepository.PaymentGetAvoir(paymentGetAvoirRequest)

            if (response.isSuccessful) {
                val paymentSuccess = response.body() ?: false

                CoroutineScope(Dispatchers.Main).launch {
                    delay(5)
                    if (paymentSuccess) {
                        navController.navigate("PaymentSuccessScreen")
                    } else {
                        navController.navigate("payment_error_screen")
                    }
                }

                DataResult2.Success(paymentSuccess)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"

                CoroutineScope(Dispatchers.Main).launch {
                    delay(5)
                    navController.navigate("server_error_screen")
                }
                DataResult2.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(5)
                navController.navigate("server_error_screen")
            }

            DataResult2.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
        }
    }
}

class PaymentPartBookingUseCase @Inject constructor(private val paymentGetAvoirRepository: IPaymentPartBookingRepository) {

    suspend fun PaymentPartBooking (paymentGetAvoirRequest: PaymentPartBookingRequest, navController: NavController): DataResult2<Boolean>  {
        return try {
            val response = paymentGetAvoirRepository.PaymentPartBooking(paymentGetAvoirRequest)
            if (response.isSuccessful) {
                val paymentSuccess = response.body() ?: false

                CoroutineScope(Dispatchers.Main).launch {
                    delay(5)
                    if (paymentSuccess) {
                        navController.navigate("PaymentSuccessScreen")
                    } else {
                        navController.navigate("payment_error_screen")
                    }
                }

                DataResult2.Success(paymentSuccess)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"

                CoroutineScope(Dispatchers.Main).launch {
                    delay(5)
                    navController.navigate("server_error_screen")
                }
                DataResult2.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {

            CoroutineScope(Dispatchers.Main).launch {
                delay(5)
                navController.navigate("server_error_screen")
            }

            DataResult2.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
        }
    }
}

class PaymentParCreditUseCase @Inject constructor(private val paymentParCreditRepository: IPaymentParCreditRepository) {

    suspend fun PaymentParCredit(paymentParCreditRequest: PaymentParCreditRequest): DataResult {
        return try {
            val response = paymentParCreditRepository.PaymentParCredit(paymentParCreditRequest)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during login")
        }
    }
}