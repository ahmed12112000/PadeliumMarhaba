package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.padelium.data.dto.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.nevadev.padeliummarhaba.R
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.PlanningDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.collectAsState
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import androidx.compose.ui.Alignment

@Composable
fun TimeSlotSelector(
    timeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?
) {
    val uniqueTimeSlots = timeSlots
        .distinctBy { it.time }
        .sortedBy { it.time }

    val firstTimeSlot = uniqueTimeSlots.firstOrNull()?.time?.format(DateTimeFormatter.ofPattern("H:mm"))

    val selectedTime = remember { mutableStateOf(selectedTimeSlot ?: firstTimeSlot) }

    LaunchedEffect(firstTimeSlot) {
        firstTimeSlot?.let {
            onTimeSlotSelected(it)
            selectedTime.value = it
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uniqueTimeSlots) { timeSlot ->
            val formattedTime = timeSlot.time.format(DateTimeFormatter.ofPattern("H:mm"))
            val isSelected = selectedTime.value == formattedTime

            Button(
                onClick = {
                    selectedTime.value = formattedTime
                    onTimeSlotSelected(formattedTime)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color(0xFF0054D8) else Color.White
                ),
                border = if (!isSelected) {
                    BorderStroke(1.dp, Color.Black)
                } else {
                    null
                },
                modifier = Modifier
                    .size(30.dp)
            ) {
                Text(
                    text = formattedTime,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}



@Composable
fun ReservationOptions(
    onReservationSelected: (ReservationOption) -> Unit,
    key: String?,
    navController: NavController,
    viewModel: GetBookingViewModel = hiltViewModel(),
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedTimeSlot: String?,
    viewModel2: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    paymentPayAvoirViewModel : PaymentPayAvoirViewModel,
    getReservationViewModel: GetReservationViewModel

) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val isUserLoggedIn by sessionManager.isLoggedInFlow.collectAsState()
    var amountSelected by remember { mutableStateOf<Double?>(null) }
    var currencySymbol by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    val dataResultBooking by viewModel.dataResultBooking.observeAsState(initial = DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())
    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }


        when (val result = dataResultBooking) {
            is DataResultBooking.Loading -> LoadingState()
            is DataResultBooking.Success -> SuccessState(
                establishmentsList = result.data,
                filteredTimeSlots = filteredTimeSlots.distinctBy { it.time },
                isUserLoggedIn = isUserLoggedIn,
                navController = navController,
                bookingViewModel = bookingViewModel,
                saveBookingViewModel = viewModel2,
                onReservationSelected = onReservationSelected,
                setAmountSelected = { amountSelected = it },
                setCurrencySymbol = { currencySymbol = it },
                showLoginPopup = { showLoginPopup = it },
                selectedTimeSlot = selectedTimeSlot,
                onTimeSlotSelected = { selectedTimeSlot = it },
                paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                selectedDate = selectedDate
            )

            is DataResultBooking.Failure -> FailureState(
                result.errorMessage,
                setErrorMessage = { errorMessage = it })
        }
        if (showLoginPopup) {
        }
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
    }


@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun SuccessState(
    establishmentsList: List<GetBookingResponseDTO>,
    filteredTimeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?,
    isUserLoggedIn: Boolean,
    navController: NavController,
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    saveBookingViewModel: SaveBookingViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    showLoginPopup: (Boolean) -> Unit,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    selectedDate: LocalDate
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 253.dp)
    ) {
        TimeSlotSelector(
            timeSlots = filteredTimeSlots,
            onTimeSlotSelected = onTimeSlotSelected,
            selectedTimeSlot = selectedTimeSlot
        )
        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(establishmentsList) { getBookingResponseDTO ->
                EstablishmentCard(
                    getBookingResponseDTO = getBookingResponseDTO,
                    filteredTimeSlots = filteredTimeSlots,
                    navController = navController,
                    bookingViewModel = bookingViewModel,
                    saveBookingViewModel = saveBookingViewModel,
                    setAmountSelected = setAmountSelected,
                    setCurrencySymbol = setCurrencySymbol,
                    selectedTimeSlot = selectedTimeSlot,
                    paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                    selectedDate = selectedDate
                )
            }
        }
    }
}

@Composable
private fun EstablishmentCard(
    getBookingResponseDTO: GetBookingResponseDTO,
    filteredTimeSlots: List<TimeSlot>,
    navController: NavController,
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    saveBookingViewModel: SaveBookingViewModel,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedTimeSlot: String?,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    selectedDate: LocalDate
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val isUserLoggedIn by sessionManager.isLoggedInFlow.collectAsState()
    val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
        planning.fromStr == selectedTimeSlot
    }
    availablePlannings.forEach { planning ->
        val amountToShow = if (planning.reductionPrice != null && planning.reductionPrice != BigDecimal.ZERO) {
            planning.reductionPrice
        } else {
            getBookingResponseDTO.amount ?: BigDecimal.ZERO
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    handleCardClick(
                        selectedBooking = getBookingResponseDTO,
                        planning = planning,
                        bookingViewModel = bookingViewModel,
                        navController = navController,
                        saveBookingViewModel = saveBookingViewModel,
                        viewModel1 = viewModel1,
                        onReservationSelected = { reservationOption -> },
                        paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                        amountToShow = amountToShow,
                        context = context,
                        selectedDate = selectedDate,
                        isUserLoggedIn = isUserLoggedIn,
                        sessionmanager = sessionManager
                    )
                },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Gray),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            EstablishmentCardContent(getBookingResponseDTO, planning)
        }
    }
}


@Composable
private fun EstablishmentCardContent(getBookingResponseDTO: GetBookingResponseDTO, planning: PlanningDTO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logopadelium),
            contentDescription = "Establishment Icon",
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF0054D8), shape = CircleShape)
                .padding(4.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(7.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = getBookingResponseDTO.establishmentDTO?.name ?: "Unknown",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "${planning.fromStr} - ${planning.toStr}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "90 min",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            val amountToShow = getBookingResponseDTO.amount ?: BigDecimal.ZERO

            if (planning.reductionPrice != null && planning.reductionPrice != BigDecimal.ZERO) {
                Box(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "${String.format("%.2f", amountToShow)} DT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0054D8),
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                    ) {
                        val textHeight = size.height / 2
                        drawLine(
                            color = Color.Red,
                            strokeWidth = 6f,
                            start = Offset(0f, textHeight),
                            end = Offset(size.width, textHeight)
                        )
                    }
                }

                Text(
                    text = "${String.format("%.2f", planning.reductionPrice)} DT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "${String.format("%.2f", amountToShow)} DT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0054D8),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun FailureState(errorMessage: String?, setErrorMessage: (String) -> Unit) {
    setErrorMessage(errorMessage ?: "An unexpected error occurred.")
}
fun handleCardClick(
    selectedBooking: GetBookingResponseDTO,
    planning: PlanningDTO,
    bookingViewModel: GetBookingViewModel,
    navController: NavController,
    saveBookingViewModel: SaveBookingViewModel,
    viewModel1: ExtrasViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    amountToShow: BigDecimal,
    context: Context,
    selectedDate: LocalDate,
    isUserLoggedIn: Boolean,
    sessionmanager: SessionManager
) {
    val establishmentName = selectedBooking.establishmentDTO?.name ?: "Unknown"
    val selectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"
    val updatedBooking = selectedBooking.copy(plannings = listOf(planning))
    val encodedDate = Uri.encode(selectedDate.toString())
    val mappedBookingsJson = Uri.encode(Gson().toJson(listOf(updatedBooking).toDomain()))
    val reservationOption = ReservationOption(
        name = establishmentName,
        time = selectedTimeSlot,
        price = amountToShow.toString(),
        mappedBookings = mappedBookingsJson
    )
    val destinationUrl = "payment_section1/${Uri.encode(reservationOption.name)}/" +
            "${Uri.encode(reservationOption.time)}/" +
            "${Uri.encode(reservationOption.price)}/" +
            "$mappedBookingsJson/$encodedDate"

    if (!sessionmanager.isLoggedIn()) {
        onReservationSelected(reservationOption)
        val loginDestination = "login_screen?redirectUrl=${Uri.encode(destinationUrl)}"
        navController.navigate(loginDestination) {
            popUpTo("main_screen") { inclusive = false }
        }
    } else {
        navController.navigate(destinationUrl) {
            popUpTo("main_screen") { inclusive = false }
        }
    }
}










fun <T> List<T>.toJson(): String {
    return Gson().toJson(this)
}
