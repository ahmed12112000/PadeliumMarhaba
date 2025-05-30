package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padelium.data.dto.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate


@Composable
fun PopupCredit(
    onPayClick: () -> Unit,
    onCancelClick: () -> Unit,
    viewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    errorCreditViewModel: ErrorCreditViewModel = hiltViewModel(),
    onTotalAmountCalculated: (Double, String) -> Unit,
    adjustedAmount: Double,
    totalExtrasCost: Double,
    showPopup: Boolean,
    onDismiss: () -> Unit,
    mappedBookingsJson: String,
    viewModel9: SharedViewModel,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedReservation: ReservationOption,
    saveBookingViewModel: SaveBookingViewModel = hiltViewModel(),
    bookingId: String?,
    playerIds: List<Long>,
    sharedExtrass : List<Long>,
    privateExtrass : List<Long>
) {
    if (!showPopup) return
    val confirmBookingViewModel: ConfirmBookingViewModel = hiltViewModel()
    val type = object : TypeToken<List<GetBookingResponse>>() {}.type
    val selectedParts by viewModel9.selectedParts.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    // NEW: Add a flag to track if payment is already being processed
    var isPaymentProcessing by remember { mutableStateOf(false) }
    val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)
    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf(BigDecimal.ZERO) }
    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val balanceData by balanceViewModel.dataResult.observeAsState(DataResult.Loading)
    val totalAmountSelected = adjustedAmount + totalExtrasCost
    onTotalAmountCalculated(totalAmountSelected, "DT")
    Log.d("totalAmount","$totalAmountSelected")
    var elapsedTime by remember { mutableStateOf(0f) }
    val totalTime = 240f
    val timeLeft = (totalTime - elapsedTime).toInt()

    val animatedColor by animateColorAsState(
        targetValue = when {
            timeLeft > 60 -> Color(0xFF4CAF50)
            timeLeft > 30 -> Color(0xFFFFC107)
            else -> Color(0xFFF44336)
        },
        animationSpec = tween(durationMillis = 500)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = elapsedTime / totalTime,
        animationSpec = tween(durationMillis = 500)
    )

    val creditErrorRequest = bookingId?.toLongOrNull()?.let { id ->
        CreditErrorRequest(
            amount = BigDecimal.ZERO,
            bookingIds = listOf(id),
            buyerId = 0L,
            payFromAvoir = false,
            status = true,
            token = "",
            transactionId = 0L
        )
    } ?: CreditErrorRequest(
        amount = BigDecimal.ZERO,
        bookingIds = emptyList(),
        buyerId = 0L,
        payFromAvoir = false,
        status = false,
        token = "",
        transactionId = 0L
    )

    LaunchedEffect(bookingId) {
        bookingId?.toLongOrNull()?.let { id ->
        }
    }

    LaunchedEffect(bookingId) {
        bookingId?.toLongOrNull()?.let { id ->
            viewModel.fetchProfileData()
            balanceViewModel.fetchAndBalance()

            while (elapsedTime < totalTime) {
                delay(1000)
                elapsedTime += 1f
            }

            val request = CreditErrorRequest(
                amount = BigDecimal.ZERO,
                bookingIds = listOf(id),
                buyerId = 0L,
                payFromAvoir = false,
                status = true,
                token = "",
                transactionId = 0L
            )
            errorCreditViewModel.ErrorCredit(request)
            onDismiss()
        }
    }

    when (val result = profileData) {
        is DataResult.Success -> {
            val profile = result.data as? GetProfileResponse
            if (profile != null) {
                firstName = profile.firstName
                lastName = profile.lastName
            }
        }
        is DataResult.Failure ->{}
        else -> Unit
    }

    when (val result = balanceData) {
        is DataResult.Success -> balance = result.data as? BigDecimal ?: BigDecimal.ZERO
        is DataResult.Failure ->{}
        else -> Unit
    }

    Dialog(
        onDismissRequest = {
            if (creditErrorRequest != null) {
                errorCreditViewModel.ErrorCredit(creditErrorRequest)
            }
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .padding(16.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = {
                                onDismiss()
                                if (creditErrorRequest != null) {
                                    errorCreditViewModel.ErrorCredit(creditErrorRequest)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Red
                            )
                        }
                    }

                    Text(
                        text = "Paiement - Padelium Marhaba",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$firstName $lastName", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Votre solde: $balance Crédits",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val coroutineScope = rememberCoroutineScope()
                    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    Button(
                        onClick = {
                            // FIXED: Prevent multiple clicks
                            if (isLoading || isPaymentProcessing) return@Button

                            // Set the processing flag immediately
                            isPaymentProcessing = true
                            isLoading = true

                            viewModel9.updateSelectedParts(selectedParts)

                            coroutineScope.launch {
                                try {
                                    viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                        val selectedBooking = mappedBookings.firstOrNull()

                                        if (selectedBooking != null) {
                                            if (totalAmountSelected <= 0) {
                                                isLoading = false
                                                isPaymentProcessing = false
                                                return@collectLatest
                                            }
                                            val orderIdValue = bookingId?.toLongOrNull()?.toString() ?: ""

                                            if (orderIdValue.isEmpty() || orderIdValue == "0") {
                                                errorMessage = "Cette réservation n'est pas disponible pour le moment. Veuillez réessayer plus tard."
                                                isLoading = false
                                                isPaymentProcessing = false
                                                return@collectLatest
                                            }

                                            val playerIds = selectedPlayers.toList()
                                            val totalAmountBigDecimal =
                                                BigDecimal.valueOf(totalAmountSelected)
                                                    .setScale(0, RoundingMode.DOWN)
                                            val currency =
                                                selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                            onTotalAmountCalculated(
                                                totalAmountBigDecimal.toInt().toDouble(), currency
                                            )

                                            paymentPayAvoirViewModel.PaymentPayAvoir(
                                                totalAmountBigDecimal
                                            )

                                            paymentPayAvoirViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                                when (paymentResult) {
                                                    is DataResult.Loading -> {}

                                                    is DataResult.Success -> {
                                                        val payFromAvoirResponse = true
                                                        balanceViewModel.fetchAndBalance()
                                                        val paymentRequest = PaymentRequest(
                                                            amount = totalAmountSelected.toString(),
                                                            currency = "DT",
                                                            orderId = bookingId?.toLongOrNull()
                                                                ?.toString() ?: ""
                                                        )

                                                        paymentViewModel.Payment(paymentRequest)

                                                        paymentViewModel.dataResult.observe(
                                                            lifecycleOwner
                                                        ) { paymentResult ->
                                                            when (paymentResult) {
                                                                is DataResult.Success -> {
                                                                    val confirmBookingRequest =
                                                                        ConfirmBookingRequest(
                                                                            amount = totalAmountSelected.toBigDecimal(),
                                                                            numberOfPart = currentSelectedParts,
                                                                            payFromAvoir = payFromAvoirResponse,
                                                                            privateExtrasIds = privateExtrass,
                                                                            bookingIds = listOfNotNull(bookingId?.toLongOrNull()),
                                                                            buyerId = "",
                                                                            couponIds = emptyMap(),
                                                                            sharedExtrasIds = sharedExtrass,
                                                                            status = true,
                                                                            token = "",
                                                                            transactionId = "",
                                                                            userIds = playerIds
                                                                        )
                                                                    confirmBookingViewModel.GetPayment(
                                                                        confirmBookingRequest
                                                                    )

                                                                    confirmBookingViewModel.dataResult.observe(
                                                                        lifecycleOwner
                                                                    ) { confirmResult ->
                                                                        when (confirmResult) {
                                                                            is DataResult.Success -> {
                                                                                isLoading = false
                                                                                isPaymentProcessing = false
                                                                                navController.navigate("PaymentSuccessScreen")
                                                                            }

                                                                            is DataResult.Failure -> {
                                                                                isLoading = false
                                                                                isPaymentProcessing = false
                                                                            }

                                                                            is DataResult.Loading -> {
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                is DataResult.Failure -> {
                                                                    isLoading = false
                                                                    isPaymentProcessing = false
                                                                }

                                                                is DataResult.Loading -> {
                                                                }
                                                            }
                                                        }
                                                    }

                                                    is DataResult.Failure -> {
                                                        isLoading = false
                                                        isPaymentProcessing = false
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Reset flags in case of any exception
                                    isLoading = false
                                    isPaymentProcessing = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                        shape = RoundedCornerShape(8.dp),
                        // FIXED: Disable button when processing OR when insufficient balance
                        enabled = !isPaymentProcessing && !isLoading && totalAmountSelected <= balance.toDouble()
                    ) {
                        // FIXED: Show different text when processing
                        if (isPaymentProcessing || isLoading) {
                            Text(
                                text = "Traitement en cours...",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Payer: $totalAmountSelected Crédits",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {
                            if (creditErrorRequest != null) {
                                errorCreditViewModel.ErrorCredit(creditErrorRequest)
                            }
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        // FIXED: Also disable cancel button when processing to prevent confusion
                        enabled = !isPaymentProcessing
                    ) {
                        Text(
                            text = "Annuler et retourner à la page de recherche",
                            color = if (isPaymentProcessing) Color.Gray else Color(0xFF0054D8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confidentialité",
                                tint = Color(0xFF0054D8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Le paiement par compte Padelium est géré par la société DevoPro",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = "Paiement sécurisé",
                                tint = Color(0xFF0054D8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ce mode de paiement n'engendre pas de frais supplémentaires",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Timer",
                            tint = Color(0xFF0054D8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Fermeture automatique dans ${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        color = animatedColor
                    )
                }
            }
        }
    }
}




