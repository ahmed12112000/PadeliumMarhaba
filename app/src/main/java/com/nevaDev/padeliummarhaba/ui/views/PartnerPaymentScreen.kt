package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentParCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.PrivateExtrasResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate


@Composable
fun PartnerPaymentScreen(
    navController: NavController,
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    viewModel3: PaymentPartViewModel = hiltViewModel(),
    partnerPayId: String?,
    viewModel2: PaymentParCreditViewModel = hiltViewModel(),
    errorCreditViewModel: ErrorCreditViewModel = hiltViewModel(),

    ) {

    var showPopup by remember { mutableStateOf(false) }
    val viewModel: GetProfileViewModel = hiltViewModel()
    var totalPrice by remember { mutableStateOf(BigDecimal.ZERO) }
    var isLoading by remember { mutableStateOf(false) }
    val partnerPayResponse by viewModel4.partnerPayResponse.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Quadruple<String, String, Int, Long>>>(emptyList()) }
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }
    var buttonCardClickInProgress by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()


    LaunchedEffect(partnerPayId) {
        partnerPayId?.let {
            viewModel4.partnerPay(it.toLong())
        }
    }
    fun updateExtras(newExtras: List<Quadruple<String, String, Int, Long>>, newTotalExtrasCost: Double) {
        selectedExtras = newExtras
        totalExtrasCost = newTotalExtrasCost
    }
    fun handleTotalPriceCalculated(newTotalPrice: BigDecimal) {
        totalPrice = newTotalPrice
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState) // Add vertical scroll here
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ExtrasSection2(onExtrasUpdate = ::updateExtras, privateList = privateList)
        }

        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            partnerPayResponse?.let { response ->
                ReservationSummary2(
                    viewModel4 = viewModel4,
                    selectedExtras = selectedExtras,
                    totalExtrasCost = totalExtrasCost,
                    onTotalPriceCalculated = { newTotalPrice ->
                        handleTotalPriceCalculated(newTotalPrice)
                    }
                )

            } ?: run {
                Text(
                    text = "No reservation selected",
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
            Text(
                text = "Acheter des crédits.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                var errorMessage by remember { mutableStateOf("") }
                val coroutineScope = rememberCoroutineScope()

                Row(modifier = Modifier.weight(1.1f)) {


                    Button(
                        onClick = {
                            if (!buttonCardClickInProgress) {
                                buttonCardClickInProgress = true

                                coroutineScope.launch {
                                    try {
                                        val currentResponse = viewModel4.partnerPayResponse.value

                                        if (currentResponse != null) {
                                            val totalAmount = (currentResponse.amount + totalExtrasCost.toBigDecimal())
                                                .setScale(2, RoundingMode.HALF_UP)

                                            val paymentRequest = PaymentRequest(
                                                amount = totalAmount.toString(),
                                                currency = "DT",
                                                orderId = currentResponse.id.toString()
                                            )

                                            // Call payment
                                            viewModel3.PaymentPart(paymentRequest)

                                            // Wait for result and handle it
                                            viewModel3.dataResult.value?.let { result ->
                                                when (result) {
                                                    is DataResult.Success -> {
                                                        val paymentResponse = result.data as? PaymentResponse
                                                        Log.d("PaymentResponse", "Full response: $paymentResponse")
                                                        Log.d("PaymentResponse", "FormUrl: '${paymentResponse?.formUrl}'")
                                                        Log.d("PaymentResponse", "PayUrl: '${paymentResponse?.payUrl}'")
                                                        Log.d("PaymentResponse", "OrderId: '${paymentResponse?.orderId}'")
                                                        Log.d("PaymentResponse", "ErrorCode: '${paymentResponse?.errorCode}'")
                                                        Log.d("PaymentResponse", "ErrorMessage: '${paymentResponse?.errorMessage}'")

                                                        // Try formUrl first, then payUrl as fallback
                                                        val formUrl = paymentResponse?.formUrl?.takeIf { it.isNotEmpty() }
                                                            ?: paymentResponse?.payUrl?.takeIf { it.isNotEmpty() }
                                                        val orderId = paymentResponse?.orderId

                                                        if (!formUrl.isNullOrEmpty() && !orderId.isNullOrEmpty()) {
                                                            // Success case - navigate to WebView
                                                            val encodedUrl = Uri.encode(formUrl)
                                                            val encodedBookingId = Uri.encode(paymentRequest.orderId)
                                                            val privateListString = privateList.value.joinToString(",")
                                                            val encodedPrivateList = Uri.encode(privateListString)
                                                            val encodedPartnerPayId = Uri.encode(partnerPayId ?: "")

                                                            val navigationRoute = "WebViewScreen2?formUrl=$encodedUrl&orderId=$orderId&BookingId=$encodedBookingId&privateList=$encodedPrivateList&encodedPartnerPayId=$encodedPartnerPayId"
                                                            navController.navigate(navigationRoute)
                                                            errorMessage = ""
                                                        } else {
                                                            // Error case - formUrl is null or empty
                                                            // ErrorCredit is already called in the ViewModel
                                                            Log.e("PaymentError", "FormUrl and PayUrl are both null/empty. Navigating to error screen.")
                                                            navController.navigate("ErrorPayement_screen")
                                                            errorMessage = "Cette réservation n'est pas disponible pour le moment."
                                                        }
                                                    }

                                                    is DataResult.Failure -> {
                                                        Log.e("PaymentError", "Payment API failed: ${result.errorMessage}")

                                                        // Call ErrorCredit for API failure (this case is not handled in ViewModel)
                                                        val errorRequest = CreditErrorRequest(
                                                            amount = BigDecimal.ZERO,
                                                            bookingIds = listOf(currentResponse.id),
                                                            buyerId = 0L,
                                                            payFromAvoir = false,
                                                            status = true,
                                                            token = "",
                                                            transactionId = 0L
                                                        )
                                                       // errorCreditViewModel.ErrorCredit(errorRequest)

                                                        navController.navigate("ErrorPayement_screen")
                                                        errorMessage = "Payment failed: ${result.errorMessage}"
                                                    }

                                                    is DataResult.Loading -> {
                                                        errorMessage = ""
                                                        // Don't call ErrorCredit during loading
                                                    }
                                                }
                                            } ?: run {
                                                // dataResult.value is null - this is also an error case
                                                Log.e("PaymentError", "dataResult.value is null after payment call")

                                                val errorRequest = CreditErrorRequest(
                                                    amount = BigDecimal.ZERO,
                                                    bookingIds = listOf(currentResponse.id),
                                                    buyerId = 0L,
                                                    payFromAvoir = false,
                                                    status = true,
                                                    token = "",
                                                    transactionId = 0L
                                                )
                                            //    errorCreditViewModel.ErrorCredit(errorRequest)

                                                navController.navigate("ErrorPayement_screen")
                                                errorMessage = "No payment response received"
                                            }
                                        } else {
                                            Log.e("PaymentError", "currentResponse is null")
                                            errorMessage = "No reservation data available"
                                        }
                                    } catch (e: Exception) {
                                        Log.e("PaymentError", "Exception in payment flow", e)

                                        // Call ErrorCredit for exceptions
                                        val currentResponse = viewModel4.partnerPayResponse.value
                                        if (currentResponse != null) {
                                            val errorRequest = CreditErrorRequest(
                                                amount = BigDecimal.ZERO,
                                                bookingIds = listOf(currentResponse.id),
                                                buyerId = 0L,
                                                payFromAvoir = false,
                                                status = true,
                                                token = "",
                                                transactionId = 0L
                                            )
                                          //  errorCreditViewModel.ErrorCredit(errorRequest)
                                        }

                                        navController.navigate("ErrorPayement_screen")
                                        errorMessage = "An error occurred during payment"
                                    } finally {
                                        buttonCardClickInProgress = false
                                    }
                                }
                            }
                        },
                        enabled = !isLoading && !buttonCardClickInProgress,
                        modifier = Modifier
                            .offset(x = -15.dp)
                            .height(48.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                        shape = RoundedCornerShape(13.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = "Card Payment",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            androidx.compose.material3.Text(
                                text = "Carte Crédit",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Row(modifier = Modifier.weight(1.3f)) {

                    Button(
                        onClick = {

                            showPopup = true

                        },
                        modifier = Modifier
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                        shape = RoundedCornerShape(13.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Money,
                                contentDescription = "Credits Payment",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            androidx.compose.material3.Text(
                                text = "Crédit Padelium",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (showPopup) {
                        PopupCreditPartner(
                            navController = navController,
                            showPopup = showPopup,
                            onDismiss = { showPopup = false },
                            viewModel4 = viewModel4,
                            partnerPayId = partnerPayId,
                            viewModel = viewModel,
                            totalPrice = totalPrice,
                            selectedExtras = selectedExtras,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExtrasSection2(
    // Change the callback to include the extra ID (Long) as the fourth element
    onExtrasUpdate: (List<Quadruple<String, String, Int, Long>>, Double) -> Unit,
    viewModel4: PrivateExtrasViewModel = hiltViewModel(),
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    privateList: MutableState<MutableList<Long>>,
) {
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val privateExtrasState by viewModel4.extrasState2.observeAsState()
    // Change to include the extra ID as fourth element
    var selectedExtras by remember { mutableStateOf<List<Quadruple<String, String, Int, Long>>>(emptyList()) }
    val totalExtrasCost by remember { derivedStateOf { selectedExtras.sumOf { it.second.toDouble() } } }
    Log.d("Ahmeeeeeeeed","$totalExtrasCost")

    LaunchedEffect(viewModel4) {
        viewModel4.PrivateExtras()
    }

    Row(
        modifier = Modifier.fillMaxWidth().offset(y = (-8).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "  Je commande des extras ?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = additionalExtrasEnabled,
            onCheckedChange = { additionalExtrasEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF0054D8),
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Color(0xFF0054D8).copy(alpha = 0.5f),
                uncheckedTrackColor = Color.LightGray
            )
        )
    }

    if (additionalExtrasEnabled) {
        Spacer(modifier = Modifier.height(2.dp))

        when (privateExtrasState) {
            is DataResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is DataResult.Success -> {
                val privateExtrasList =
                    (privateExtrasState as DataResult.Success).data as? List<PrivateExtrasResponse>

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = "  Article(s) réserver à mon usage",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    privateExtrasList?.forEach { privateExtra ->
                        val isPrivateExtraAdded = privateList.value.contains(privateExtra.id)
                        Log.d("ExtrasSection2", "Added private extra ID: $privateExtra")

                        ExtraItemCard(
                            extra = privateExtra,
                            isAdded = isPrivateExtraAdded,
                            onAddClick = { extraPrice ->
                                privateList.value = (privateList.value + privateExtra.id).toMutableList()
                                findTermsViewModel.updatePrivateExtras(privateList.value)
                                Log.d("ExtrasSection2", "Added private extra ID: ${privateExtra.id}, List: $privateList")

                                // Include the extra ID as the fourth element
                                selectedExtras += Quadruple(
                                    privateExtra.name,
                                    privateExtra.amount.toString(),
                                    privateExtra.currencyId.toInt(),
                                    privateExtra.id // Add the extra ID here
                                )
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            },
                            onRemoveClick = { extraPrice ->
                                privateList.value = (privateList.value - privateExtra.id).toMutableList()
                                findTermsViewModel.updatePrivateExtras(privateList.value)
                                Log.d("ExtrasSection2", "Removed private extra ID: ${privateExtra.id}, List: $privateList")

                                selectedExtras = selectedExtras.filterNot { it.fourth == privateExtra.id }
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            }
                        )
                    }
                }
            }
            else -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Failed to load extras", color = Color.Red)
                }
            }
        }
    }
}

// You'll need to define Quadruple if it doesn't exist in your project
data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Composable
fun ReservationSummary2(
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    selectedExtras: List<Quadruple<String, String, Int, Long>>, // Now includes extra IDs
    totalExtrasCost: Double,
    onTotalPriceCalculated: (BigDecimal) -> Unit

    ) {
    val storedPartnerPayResponse by viewModel4.partnerPayResponse.observeAsState()



    LaunchedEffect(storedPartnerPayResponse) {
        storedPartnerPayResponse?.let { response ->
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Détails Réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        storedPartnerPayResponse?.let { booking ->
            val totalPrice = (booking.amount + BigDecimal(totalExtrasCost)).setScale(2, RoundingMode.HALF_UP)
            onTotalPriceCalculated(totalPrice)
            ReservationDetailRow(label = "Espace", value = booking.bookingEstablishmentName ?: "N/A")
            ReservationDetailRow(label = "Heure", value = booking.bookingDateStr ?: "N/A")
            ReservationDetailRow(label = "Prix", value = "${booking.amount} DT")
            ReservationDetailRow(
                label = "Réservé par",
                value = "${booking.bookingCreatedFirstName ?: "N/A"} ${booking.bookingCreatedLastName ?: "N/A"}"
            )
            ReservationDetailRow(
                label = "Joueurs",
                value = "${booking.bookingCreatedFirstName ?: "N/A"} ${booking.bookingCreatedLastName ?: "N/A"} ${booking.userFirstName ?: "N/A"} ${booking.userLastName ?: "N/A"}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Extras Sélectionnés",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            selectedExtras.forEach { extra ->
                ReservationDetailRow(label = extra.first, value = "${extra.second} DT")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Détails du Prix",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ReservationDetailRow(label = "Prix de Réservation", value = "${booking.amount} DT")
            ReservationDetailRow(label = "Extras Total", value = "$totalExtrasCost DT")
            ReservationDetailRow(label = "Total", value = "$totalPrice DT")

        }
    }

}






