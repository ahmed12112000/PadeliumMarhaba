
package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.usecases.ErrorCreditUseCase
import com.padelium.domain.usecases.PaymentPartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class PaymentPartViewModel @Inject constructor(
    private val paymentPartUseCase: PaymentPartUseCase,
    private val errorCreditUseCase: ErrorCreditUseCase // Inject the ErrorCredit use case
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    fun PaymentPart(paymentRequest: PaymentRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                val result = paymentPartUseCase.PaymentPart(paymentRequest)

                if (result is DataResult.Success) {
                    val paymentResponse = result.data as? PaymentResponse

                    // Check if formUrl and payUrl are both null or empty
                    val formUrl = paymentResponse?.formUrl?.takeIf { it.isNotEmpty() }
                    val payUrl = paymentResponse?.payUrl?.takeIf { it.isNotEmpty() }

                    if (formUrl == null && payUrl == null) {
                        // Both URLs are null/empty, call ErrorCredit
                        Log.e("PaymentPartViewModel", "FormUrl and PayUrl are both null/empty. Calling ErrorCredit.")

                        val bookingId = paymentResponse?.orderId?.toLongOrNull()
                            ?: paymentRequest.orderId.toLongOrNull()
                            ?: 0L

                        val errorRequest = CreditErrorRequest(
                            amount = BigDecimal.ZERO,
                            bookingIds = listOf(bookingId),
                            buyerId = 0L,
                            payFromAvoir = false,
                            status = true,
                            token = "",
                            transactionId = 0L
                        )

                        // Call ErrorCredit
                        try {
                            errorCreditUseCase.ErrorCredit(errorRequest)
                            Log.d("PaymentPartViewModel", "ErrorCredit called successfully")
                        } catch (e: Exception) {
                            Log.e("PaymentPartViewModel", "Failed to call ErrorCredit", e)
                        }

                        // Still return the original result so UI can handle navigation
                        dataResult.value = result
                    } else {
                        // URLs are available, proceed normally
                        dataResult.value = result
                    }
                } else {
                    // Handle other result types (Failure, Loading)
                    dataResult.value = result
                }
            } catch (e: Exception) {
                dataResult.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "Payment failed: ${e.message}"
                )
            }
        }
    }
}




