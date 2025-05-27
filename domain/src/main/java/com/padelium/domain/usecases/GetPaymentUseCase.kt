package com.padelium.domain.usecases

import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.repositories.IGetPaymentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val getPaymentRepository: IGetPaymentRepository
) {

    suspend fun GetPayment(getPaymentRequest: GetPaymentRequest, navController: NavController): DataResult2<Boolean> {
        return try {
            val response = getPaymentRepository.GetPayment(getPaymentRequest)

            if (response.isSuccessful) {
                val paymentSuccess = response.body() ?: false

                CoroutineScope(Dispatchers.Main).launch {
                    delay(5) // Wait before navigating
                    if (paymentSuccess) {
                        navController.navigate("PaymentSuccessScreen")
                    } else {
                        navController.navigate("payment_error_screen")
                    }
                }

                DataResult2.Success(paymentSuccess)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"

                // Launch navigation in a coroutine to ensure a delay
                CoroutineScope(Dispatchers.Main).launch {
                    delay(5)
                    navController.navigate("server_error_screen")
                }

                DataResult2.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {

            // Delay navigation inside a coroutine to prevent immediate transition
            CoroutineScope(Dispatchers.Main).launch {
                delay(5)
                navController.navigate("server_error_screen")
            }

            DataResult2.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
        }
    }
}




