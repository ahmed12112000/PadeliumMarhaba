package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padelium.data.dto.ReservationOption
import java.time.LocalDate
import androidx.compose.material.*
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UpdatePhoneViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentResponse
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.padelium.domain.dto.SaveBookingResponse // Adjust import as necessary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.math.RoundingMode

fun List<GetBookingResponseDTO>.toDomain(): List<GetBookingResponse> {
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return this.map { dto ->

        val searchDate = dto.searchDate?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: LocalDate.now()
        val firstPlanning = dto.plannings?.firstOrNull()

        val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0')
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterOutput)
            } catch (e: Exception) { "2024-12-20 09:30" }
        } ?: "2024-12-20 09:30"
        val endFormatted = firstPlanning?.toStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0')
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterOutput)
            } catch (e: Exception) { "2024-12-20 09:30" }
        } ?: "2024-12-20 09:30"

        val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0')
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterWithMillis)
            } catch (e: Exception) { "2024-12-20T09:30:00.000Z" }
        } ?: "2024-12-20T09:30:00.000Z"

        val toFormatted = firstPlanning?.toStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0')
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterWithMillis)
            } catch (e: Exception) { "2024-12-20T09:30:00.000Z" }
        } ?: "2024-12-20T09:30:00.000Z"

        val formattedAmount = dto.amount?.stripTrailingZeros()?.toPlainString()?.let {
            if (it.contains(".")) {
                it.split(".")[0]
            } else {
                it
            }
        } ?: "0"

        val formattedAamount = dto.aamount?.stripTrailingZeros()?.toPlainString()?.let {
            if (it.contains(".")) {
                it.split(".")[0]
            } else {
                it
            }
        } ?: "0"

        val hasHappyHours = dto.plannings?.any { it.isHappyHours } ?: false


        GetBookingResponse(
            aamount = BigDecimal(formattedAamount),
            amount = BigDecimal(formattedAmount),
            amountfeeTrans = dto.amountfeeTrans ?: BigDecimal.ZERO,
            bookingAnnulationDTOSet = dto.bookingAnnulationDTOSet ?: emptySet(),
            end = endFormatted,
            establishmentDTO = dto.establishmentDTO,
            sharedExtrasIds = dto.sharedExtrasIds ?: emptyList(),
            privateExtrasIds = dto.privateExtrasIds ?: emptyList(),
            plannings = dto.plannings ?: emptyList(),
            from =fromFormatted,
            numberOfPart =  dto.numberOfPart ?: 0,
            currencyId = dto.currencyId ?: 0L,
            payFromAvoir = dto.payFromAvoir ?: false,
            searchDate = dto.searchDate ?: "",
            to =toFormatted,
            start = startFormatted,
            userIds = dto.userIds ,
            establishmentPacksDTO = dto.establishmentPacksDTO ?: emptyList(),


            reductionSecondAmount = dto.reductionSecondAmount ?: BigDecimal.ZERO,   // THIS IS THE AMOUNT FOR Happy HOUR  withSecondPrice
            reductionaSecondAmount = dto.reductionaSecondAmount ?: BigDecimal.ZERO,
            rsamountfeeTrans = dto.rsamountfeeTrans ?: BigDecimal.ZERO,
            samountfeeTrans = dto.samountfeeTrans ?: BigDecimal.ZERO,
            secondAamount = dto.secondAamount ?: BigDecimal.ZERO,
            withSecondPrice = hasHappyHours, // Set to true if any planning has isHappyHours = true
            secondReduction = dto.secondReduction ?: BigDecimal.ZERO,
            secondAmount = dto.secondAmount ?: BigDecimal.ZERO,


            )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun  PaymentSection1(
    selectedDate: LocalDate,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    navController: NavController,
    viewModel: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel,
    price: String,
    selectedTimeSlot: String,
    mappedBookingsJson: String,
    onTotalAmountCalculated: (Double, String) -> Unit,
    viewModel9: SharedViewModel,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    updatePhoneViewModel: UpdatePhoneViewModel = hiltViewModel(),
    getProfileViewModel: GetProfileViewModel = hiltViewModel()
) {
    val profileData by getProfileViewModel.profileData.observeAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var adjustedAmount by remember { mutableStateOf(0.0) }
    var adjustedSharedExtrasAmount by remember { mutableStateOf(0.0) }
    var totalSharedExtrasCost by remember { mutableStateOf(0.0) }
    val selectedParts by viewModel9.selectedParts.collectAsState()
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    val type = object : TypeToken<List<GetBookingResponse >>() {}.type
    val mappedBookings: List<GetBookingResponse > = Gson().fromJson(mappedBookingsJson, type)
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
    val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()
    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val viewModel2: ExtrasViewModel = hiltViewModel()
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }
    val time = remember { mutableStateOf<String?>(null) }
    val selectedExtras = remember { mutableStateListOf<Triple<String, String, Int>>() }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)
    val updatePhoneResult by updatePhoneViewModel.dataResult.observeAsState()
    var showMessage by remember { mutableStateOf(false) }

    LaunchedEffect(updatePhoneResult) {
        if (updatePhoneResult is DataResult.Success) {
            showMessage = true
            delay(3000)
            showMessage = false
        }
    }

    LaunchedEffect(Unit) {
        getProfileViewModel.fetchProfileData()
    }


    LaunchedEffect(profileData) {
        Log.d("ProfileScreen", "Profile data changed: $profileData")

        when (val result = profileData) {
            is DataResultBooking.Success -> {
                Log.d("ProfileScreen", "Success state received")
                try {
                    val profile = result.data
                    Log.d("ProfileScreen", "Profile data: $profile")

                    if (profile != null) {
                        firstName = profile.firstName ?: ""
                        lastName = profile.lastName ?: ""
                        phoneNumber = profile.phone ?: ""


                    } else {

                    }
                } catch (e: Exception) {

                }
                isLoading = false
            }
            is DataResultBooking.Loading -> {
                Log.d("ProfileScreen", "Loading state")
                isLoading = true
            }
            is DataResultBooking.Failure -> {

            }
            null -> {
                Log.d("ProfileScreen", "Profile data is null")
                isLoading = true
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Requis pour votre réservation",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        if (showMessage) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success Icon",
                    tint = Color(0xFF28A745),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Numéro changé avec succès",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.7f).offset(x = -10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .height(20.dp)
                    ) {
                        Text(
                            text = if (phoneNumber.isEmpty()) "* Obligatoire" else phoneNumber,
                            color = if (phoneNumber.isEmpty()) Color.Red else Color.Black,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }

                Button(
                    onClick = { showPopup = true },
                    shape = RoundedCornerShape(13.dp),
                    border = BorderStroke(1.dp, Color(0xFF0054D8)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .wrapContentHeight()
                        .size(width = 120.dp, height = 45.dp)
                ) {
                    Text(
                        text = if (phoneNumber.isEmpty()) "Ajouter" else "Modifier",
                        color = Color(0xFF0054D8),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )





                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val popupHeight = screenHeight / 4


                    if (showPopup) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Dialog(onDismissRequest = { showPopup = false }) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    elevation = 8.dp
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        OutlinedTextField(
                                            value = phoneNumber,
                                            onValueChange = { newValue ->
                                                if (newValue.all { it.isDigit() } && newValue.length <= 8) {
                                                    phoneNumber = newValue
                                                }
                                            },
                                            label = { Text("Numéro de téléphone") },
                                            modifier = Modifier.fillMaxWidth(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            isError = phoneNumber.isEmpty() || phoneNumber.length != 8 || !phoneNumber.all { it.isDigit() }, // Display error if not valid
                                            shape = RoundedCornerShape(10.dp),
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = Color.Gray,
                                                unfocusedBorderColor = Color.Gray,
                                                focusedLabelColor = Color.Gray,
                                            )
                                        )

                                        if (phoneNumber.isEmpty() || phoneNumber.length != 8 || !phoneNumber.all { it.isDigit() }) {
                                            Text(
                                                text = "* Numéro de téléphone non valide",
                                                color = Color.Red,
                                                style = MaterialTheme.typography.body2,
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Button(
                                            onClick = {
                                                if (phoneNumber.length == 8 && phoneNumber.all { it.isDigit() }) {
                                                    val phoneBody = RequestBody.create(
                                                        "text/plain".toMediaTypeOrNull(), phoneNumber
                                                    )
                                                    updatePhoneViewModel.UpdatePhone(phoneBody)
                                                    showPopup = false
                                                } else {
                                                    }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(13.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color(0xFF0054D8)
                                            ),
                                        ) {
                                            Text(
                                                text = "Sauvegarder",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }}}

        Spacer(modifier = Modifier.height(16.dp))

        ReservationDetailsCard(
            selectedExtras = selectedExtras,
            onExtrasUpdate = { updatedExtras, updatedTotalCost ->
                selectedExtras.clear()
                selectedExtras.addAll(updatedExtras)

            },
            selectedReservation = selectedReservation,
            viewModel2 = viewModel2,
            amountSelected = Pair(
                selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0,
                selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
            ),
            onAdjustedAmountUpdated = { newAdjustedAmount, newAdjustedSharedExtrasAmount ->
                adjustedAmount = newAdjustedAmount
                adjustedSharedExtrasAmount = newAdjustedSharedExtrasAmount
            },
            onPartsSelected = { newSelectedParts ->
            },
            viewModel1 = viewModel9
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }

                ExtrasSection(
                    onExtrasUpdate = { extras, cost ->
                        selectedExtras = extras
                        totalExtrasCost = extras.sumOf { it.third * it.second.toDouble() }
                    },
                    selectedParts = selectedParts.toString(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
Log.d("fffffffffff","$totalSharedExtrasCost")
                ReservationSummary(
                    selectedDate = selectedDate,
                    selectedTimeSlot = selectedTimeSlot.toString(),
                    selectedReservation = selectedReservation,
                    selectedExtras = selectedExtras,
                    amountSelected = Pair(
                        selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0,
                        selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                    ),
                    onTotalAmountCalculated = { totalAmount ->
                    },
                    price = price,
                    time = time.toString(),
                    navController = navController,
                    adjustedAmount = adjustedAmount,
                    adjustedSharedExtrasAmount = adjustedSharedExtrasAmount,
                    totalSharedExtrasCost = totalSharedExtrasCost,
                    totalExtrasCost = totalExtrasCost
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)
            ) {

                val coroutineScope = rememberCoroutineScope()
                val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
                val sharedExtras by findTermsViewModel.sharedExtras.observeAsState(initial = mutableListOf())
                val privateExtras by findTermsViewModel.privateExtras.observeAsState(initial = mutableListOf())
                val formatterOutput: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val formatterWithMillis: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val playerIds = selectedPlayers.toList()
                var errorMessage by remember { mutableStateOf<String?>(null) }
                LaunchedEffect(errorMessage) {
                    if (!errorMessage.isNullOrEmpty()) {
                    }
                }


                Button(
                    onClick = {

                        if (isLoading) return@Button


                        viewModel9.updateSelectedParts(selectedParts)

                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                val selectedBooking = mappedBookings.firstOrNull()

                                if (selectedBooking != null) {
                                    val searchDate = selectedDate

                                    val firstPlanning = selectedBooking.plannings?.firstOrNull()

                                    val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val endFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)

                                    val toFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)


                                    val totalAmountSelected = adjustedAmount + totalExtrasCost

                                    if (totalAmountSelected <= 0) {
                                        isLoading = false
                                        return@collectLatest
                                    }
                                    val formattedAmount = selectedBooking.amount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattedAamount = selectedBooking.aamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val playerIds = selectedPlayers.toList()


                                    val formattesecondAamount = selectedBooking.secondAamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattereductionSecondAmount = selectedBooking.reductionSecondAmount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formatedwithSecondPrice = selectedBooking.withSecondPrice

                                    val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->

                                        if (index == 0) {
                                            booking.copy(
                                                numberOfPart = currentSelectedParts,
                                                userIds = playerIds,
                                                searchDate = searchDate.toString(),
                                                start = startFormatted,
                                                end = endFormatted,
                                                from = fromFormatted,
                                                to = toFormatted,
                                                sharedExtrasIds = sharedExtras,
                                                privateExtrasIds = privateExtras,
                                                amount = BigDecimal(formattedAmount),
                                                aamount = BigDecimal(formattedAamount),


                                                reductionSecondAmount = formattereductionSecondAmount.toBigDecimal(),
                                                secondAamount = BigDecimal(formattesecondAamount),
                                                withSecondPrice = formatedwithSecondPrice,
                                            )
                                        } else {
                                            booking
                                        }
                                    }

                                    val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                    onTotalAmountCalculated(totalAmountSelected, currency)

                                    saveBookingViewModel.SaveBooking(updatedMappedBookings)

                                    saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                        when (result) {
                                            is DataResult.Loading -> {
                                            }

                                            is DataResult.Success -> {
                                                isLoading = false

                                                val bookingList = result.data as? List<SaveBookingResponse>

                                                if (!bookingList.isNullOrEmpty()) {
                                                    val firstBooking = bookingList[0]
                                                    val bookingId = firstBooking.id.toString()

                                                    if (bookingId == null || bookingId == "0") {

                                                      //  errorMessage = "Cette réservation n'est pas disponible pour le moment. Veuillez réessayer plus tard."
                                                        return@observe
                                                    }


                                                    val paymentRequest = PaymentRequest(
                                                        amount = totalAmountSelected.toString(),
                                                        currency =  "DT",
                                                        orderId = bookingId
                                                    )

                                                    paymentViewModel.Payment(paymentRequest)
                                                    paymentViewModel.dataResult.observe(
                                                        lifecycleOwner
                                                    ) { paymentResult ->
                                                        when (paymentResult) {
                                                            is DataResult.Loading -> {

                                                            }

                                                            is DataResult.Success -> {
                                                                isLoading = false

                                                                val paymentResponse = paymentResult.data as? PaymentResponse
                                                                val formUrl = paymentResponse?.formUrl

                                                                if (!formUrl.isNullOrEmpty()) {
                                                                    val encodedUrl = Uri.encode(formUrl)
                                                                    val userIdsString = selectedPlayers.joinToString(",")
                                                                    val encodedUserIds = Uri.encode(userIdsString)
                                                                    val SharedListString = sharedExtras.joinToString(",")
                                                                    val encodedSharedList = Uri.encode(SharedListString)
                                                                    val PrivateListString = privateExtras.joinToString(",")
                                                                    val encodedPrivateList = Uri.encode(PrivateListString)
                                                                    val encodedBookingId = Uri.encode(bookingId)
                                                                    val navigationRoute =
                                                                        "WebViewScreen?paymentUrl=$encodedUrl&numberOfPart=$currentSelectedParts&userIds=$encodedUserIds&sharedList=$encodedSharedList&privateList=$encodedPrivateList&bookingId=$encodedBookingId"
                                                                    navController.navigate(
                                                                        navigationRoute
                                                                    )
                                                                } else {
                                                                    isLoading = false

                                                                }
                                                            }

                                                            is DataResult.Failure -> {
                                                                isLoading = false
                                                                errorMessage = "Cette réservation n'est pas disponible pour le moment. Veuillez réessayer plus tard."

                                                            }
                                                        }
                                                    }
                                                } else {
                                                    isLoading = false
                                                    errorMessage = "Une erreur s'est produite. Veuillez réessayer."

                                                }
                                            }

                                            is DataResult.Failure -> {
                                                isLoading = false

                                            }
                                        }
                                    }
                                } else {
                                    isLoading = false

                                }
                            }
                        }
                    },
                    enabled = (phoneNumber.isEmpty() &&  updatePhoneResult is DataResult.Success && phoneNumber.length == 8 && phoneNumber.all { it.isDigit() } ) || (phoneNumber.isNotEmpty() && phoneNumber.length == 8 && phoneNumber.all { it.isDigit() }) ,

                    modifier = Modifier
                        .height(48.dp).offset(x = -28.dp)
                        .weight(1.2f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Card Payment",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).offset(x=-7.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Carte Crédit",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red

                    )
                }
                var showPopup by remember { mutableStateOf(false) }
                var bookingId by remember { mutableStateOf<String?>(null) }
                var hasFetchedSelectedParts by remember { mutableStateOf(false) }
                var hasFetchedBooking by remember { mutableStateOf(false) }
                var hasFetchedPaymentPayAvoir by remember { mutableStateOf(false) }
                // Add these state variables
                var isPaymentInProgress by remember { mutableStateOf(false) }
                var shouldShowErrorMessage by remember { mutableStateOf(true) }
                var paymentSuccessful by remember { mutableStateOf(false) }

                var isPaymentFlow by remember { mutableStateOf(false) }
                var hasCompletedPayment by remember { mutableStateOf(false) }

// Add a LaunchedEffect to handle navigation after payment


                LaunchedEffect(showPopup) {
                    if (!showPopup) {
                        delay(300)
                    }
                }
                Button(
                    onClick = {
                        if (isLoading) return@Button

                        if (!hasFetchedSelectedParts) {
                            viewModel9.updateSelectedParts(selectedParts)
                            hasFetchedSelectedParts = true
                        }

                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                if (isLoading) return@collectLatest

                                val selectedBooking = mappedBookings.firstOrNull()
                                if (selectedBooking != null) {
                                    val searchDate = selectedDate
                                    val totalAmountSelected = adjustedAmount + totalExtrasCost
                                    val firstPlanning = selectedBooking.plannings?.firstOrNull()

                                    val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val endFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)

                                    val toFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)

                                    if (totalAmountSelected <= 0) {
                                        isLoading = false
                                        return@collectLatest
                                    }







                                    val formattesecondAamount = selectedBooking.secondAamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattereductionSecondAmount = selectedBooking.reductionSecondAmount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattedAmount = selectedBooking.amount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattedAamount = selectedBooking.aamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formatedwithSecondPrice = selectedBooking.withSecondPrice

                                    val playerIds = selectedPlayers.toList()
                                    val totalAmountBigDecimal = BigDecimal.valueOf(totalAmountSelected).setScale(0, RoundingMode.DOWN)
                                    val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }

                                    onTotalAmountCalculated(totalAmountBigDecimal.toInt().toDouble(), currency)
                                    paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)

                                    if (!hasFetchedPaymentPayAvoir) {
                                        paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)
                                        hasFetchedPaymentPayAvoir = true
                                    }

                                    paymentPayAvoirViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                        when (paymentResult) {
                                            is DataResult.Loading -> {}
                                            is DataResult.Success -> {
                                                isLoading = false
                                                val payFromAvoirResponse = true

                                                val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->
                                                    if (index == 0) {
                                                        booking.copy(
                                                            numberOfPart = currentSelectedParts,
                                                            userIds = playerIds,
                                                            payFromAvoir = payFromAvoirResponse,
                                                            searchDate = searchDate.toString(),
                                                            start = startFormatted,
                                                            end = endFormatted,
                                                            from = fromFormatted,
                                                            to = toFormatted,
                                                            sharedExtrasIds = sharedExtras,
                                                            privateExtrasIds = privateExtras,
                                                            amount = BigDecimal(formattedAmount),
                                                            aamount = BigDecimal(formattedAamount),
                                                            establishmentPacksDTO = emptyList(),
                                                            reductionSecondAmount = formattereductionSecondAmount.toBigDecimal(),
                                                            secondAamount = BigDecimal(formattesecondAamount),
                                                            withSecondPrice = formatedwithSecondPrice,

                                                            )
                                                    } else {
                                                        booking
                                                    }
                                                }

                                                balanceViewModel.fetchAndBalance()

                                                if (!hasFetchedBooking) {
                                                    hasFetchedBooking = true
                                                    saveBookingViewModel.SaveBooking(updatedMappedBookings)
                                                }
                                            }
                                            is DataResult.Failure -> {
                                                isLoading = false
                                            }
                                        }
                                    }

                                    // Observe SaveBooking Result
                                    // Modified saveBookingViewModel observer
                                    saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                        when (result) {
                                            is DataResult.Loading -> Log.d("SaveBooking", "Saving booking...")
                                            is DataResult.Success -> {
                                                isLoading = false
                                                val bookingList = result.data as? List<SaveBookingResponse>
                                                if (!bookingList.isNullOrEmpty()) {
                                                    val firstBooking = bookingList[0]
                                                    bookingId = firstBooking.id.toString()
                                                }
                                                hasFetchedBooking = false

                                                if (isPaymentFlow) {
                                                    // We're in payment flow, navigate immediately without any other logic
                                                    navController.navigate("successScreen")
                                                    isPaymentFlow = false
                                                } else {
                                                    // Normal initial flow - only show error/popup logic for initial booking
                                                    if (bookingId == null || bookingId == "0") {
                                                        errorMessage = "Cette réservation n'est pas disponible pour le moment. Veuillez réessayer."
                                                    } else {
                                                        showPopup = true
                                                    }
                                                }
                                            }
                                            is DataResult.Failure -> {
                                                isLoading = false
                                                isPaymentFlow = false
                                                hasCompletedPayment = false
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    },
                    enabled = phoneNumber.length == 8 && phoneNumber.all { it.isDigit() },
                    modifier = Modifier
                        .height(48.dp)
                        .offset(x = -7.dp)
                        .weight(1.3f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = "Credits Payment",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).offset(x = -7.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Crédit Padelium",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
// Add this LaunchedEffect to handle navigation

                if (showPopup) {
                    PopupCredit(
                        onPayClick = {
                            showPopup = false
                            isPaymentFlow = true // Mark that we're now in payment flow
                        },
                        onCancelClick = {
                            showPopup = false
                            isPaymentFlow = false
                        },
                        viewModel = hiltViewModel(),
                        navController = navController,
                        errorCreditViewModel = hiltViewModel(),
                        onTotalAmountCalculated = { amount, currency -> },
                        adjustedAmount = adjustedAmount,
                        totalExtrasCost = totalExtrasCost,
                        showPopup = showPopup,
                        onDismiss = {
                            showPopup = false
                            if (!hasCompletedPayment) {
                                isPaymentFlow = false
                            }
                        },
                        mappedBookingsJson = mappedBookingsJson,
                        viewModel9 = viewModel9,
                        findTermsViewModel = hiltViewModel(),
                        selectedDate = selectedDate,
                        selectedReservation = selectedReservation,
                        saveBookingViewModel = hiltViewModel(),
                        bookingId = bookingId,
                        sharedExtrass = sharedExtras,
                        privateExtrass = privateExtras,
                        playerIds = playerIds
                    )
                }

            }
        }
    }
}


class SharedViewModel : ViewModel() {
    private val _selectedParts = MutableStateFlow(4)
    val selectedParts: StateFlow<Int> get() = _selectedParts

    fun updateSelectedParts(parts: Int) {
        _selectedParts.value = parts
    }
}



























