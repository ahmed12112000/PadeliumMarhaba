package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.padelium.data.dto.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import com.nevaDev.padeliummarhaba.viewmodels.GetInitViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.InitBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SearchListViewModel
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.InitBookingRequest
import kotlinx.coroutines.Job
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar

data class ApiChainState(
    val hasStarted: Boolean = false,
    val keyFetched: Boolean = false,
    val searchListFetched: Boolean = false,
    val initDataFetched: Boolean = false,
    val initBookingFetched: Boolean = false,
    val bookingDataFetched: Boolean = false,
    val currentStep: Int = 0,
    val currentDate: LocalDate? = null // Track which date this state is for
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    navController: NavController,
    isUserLoggedIn: Boolean = false,
    context: Context,
    sharedPreferences: SharedPreferences,
    onFetchSuccess: () -> Unit,
    viewModel: KeyViewModel = hiltViewModel(),
    getBookingViewModel: GetBookingViewModel = hiltViewModel(),
    viewModel2: GetInitViewModel = hiltViewModel(),
    viewModel3: SearchListViewModel = hiltViewModel(),
    viewModel4: InitBookingViewModel = hiltViewModel(),
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    getReservationViewModel: GetReservationViewModel
) {
    var fetchJob by remember { mutableStateOf<Job?>(null) }
    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }

    // API call tracking states
    var hasStartedApiChain by remember { mutableStateOf(false) }
    var keyFetched by remember { mutableStateOf(false) }
    var searchListFetched by remember { mutableStateOf(false) }
    var initDataFetched by remember { mutableStateOf(false) }
    var initBookingFetched by remember { mutableStateOf(false) }
    var bookingDataFetched by remember { mutableStateOf(false) }

    // UI states
    var hasNoData by remember { mutableStateOf(false) }
    var hasAvailableReservations by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var previousSelectedDate by remember { mutableStateOf<LocalDate?>(null) }

    var apiState by remember {
        mutableStateOf(
            ApiChainState(
                hasStarted = false,
                keyFetched = false,
                searchListFetched = false,
                initDataFetched = false,
                initBookingFetched = false,
                bookingDataFetched = false,
                currentStep = 0,
                currentDate = null
            )
        )
    }

    fun resetApiStates() {
        apiState = ApiChainState(
            hasStarted = false,
            keyFetched = false,
            searchListFetched = false,
            initDataFetched = false,
            initBookingFetched = false,
            bookingDataFetched = false,
            currentStep = 0,
            currentDate = null
        )
        reservationKey.value = null
        hasNoData = false
        hasAvailableReservations = false
        fetchJob?.cancel()
        isLoading = false


    }

    fun handleFailure(errorCode: Int?, step: String) {
        Log.e("ReservationScreen", "$step - Error occurred with code: $errorCode")
        isLoading = false
        resetApiStates()

        // Navigate to error screen for any non-successful HTTP status codes
        errorCode?.let {
            if (it !in 200..299) {  // Any non-2xx status code is an error
                Log.e("ReservationScreen", "$step - Navigating to error screen due to error code: $it")
                navController.navigate("server_error_screen")
            }
        }
    }

    fun startApiChain(date: LocalDate) {
        Log.d("ReservationScreen", "Starting API chain for date: $date")

        // Always reset when starting a new chain
        resetApiStates()

        apiState = apiState.copy(
            hasStarted = true,
            currentStep = 1,
            currentDate = date
        )
        isLoading = true

        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00"
        val fetchKeyRequest = FetchKeyRequest(dateTime = formattedDate)

        // Step 1: Get reservation key
        viewModel.getReservationKey(fetchKeyRequest, date)
    }

    // Handle date selection
    LaunchedEffect(selectedDate.value) {
        if (selectedDate.value != previousSelectedDate) {
            Log.d("ReservationScreen", "Date changed from $previousSelectedDate to ${selectedDate.value}")
            startApiChain(selectedDate.value)
            previousSelectedDate = selectedDate.value
        }
    }

    // Step 1: KeyViewModel observer - triggers SearchList
    viewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        Log.d("ReservationScreen", "Step 1 - KeyViewModel observer triggered - currentStep: ${apiState.currentStep}, currentDate: ${apiState.currentDate}")

        // Only process if this is for the current date being processed
        if (apiState.currentDate != selectedDate.value) {
            Log.d("ReservationScreen", "Step 1 - Ignoring result for old date: ${apiState.currentDate}, current: ${selectedDate.value}")
            return@observe
        }

        when (result) {
            is DataResultBooking.Success -> {
                if (apiState.currentStep == 1 && !apiState.keyFetched) {
                    Log.d("ReservationScreen", "Step 1 - KeyViewModel SUCCESS")
                    apiState = apiState.copy(keyFetched = true, currentStep = 2)
                    reservationKey.value = result.data.key
                    onFetchSuccess()

                    // Step 2: Call SearchList
                    reservationKey.value?.let { key ->
                        Log.d("ReservationScreen", "Step 1 - Calling SearchList with key: $key")
                        viewModel3.searchList(key)
                    }
                }
            }
            is DataResultBooking.Failure -> {
                if (apiState.currentStep == 1) {
                    handleFailure(result.errorCode, "Step 1 - KeyViewModel")
                }
            }
            is DataResultBooking.Loading -> {
                if (apiState.currentStep == 1) {
                    isLoading = true
                }
            }
        }
    }

    // Step 2: SearchListViewModel observer - triggers InitBooking
    viewModel3.dataResultBooking.observe(lifecycleOwner) { result ->
        Log.d("ReservationScreen", "Step 2 - SearchListViewModel observer triggered - currentStep: ${apiState.currentStep}, currentDate: ${apiState.currentDate}")

        // Only process if this is for the current date being processed
        if (apiState.currentDate != selectedDate.value) {
            Log.d("ReservationScreen", "Step 2 - Ignoring result for old date: ${apiState.currentDate}, current: ${selectedDate.value}")
            return@observe
        }

        when (result) {
            is DataResultBooking.Success -> {
                if (apiState.currentStep == 2 && !apiState.searchListFetched) {
                    Log.d("ReservationScreen", "Step 2 - SearchListViewModel SUCCESS")
                    apiState = apiState.copy(searchListFetched = true, currentStep = 3) // ← Step 3 now

                    // Step 3: Call InitBooking directly (skipping GetInit)
                    reservationKey.value?.let { key ->
                        Log.d("ReservationScreen", "Step 3 - Calling InitBooking with key: $key")
                        val initBookingRequest = InitBookingRequest(key = key)

                        // Update to step 4 BEFORE calling InitBooking
                        apiState = apiState.copy(currentStep = 4)

                        viewModel4.InitBooking(initBookingRequest)
                    }
                }
            }
            is DataResultBooking.Failure -> {
                if (apiState.currentStep == 2) {
                    handleFailure(result.errorCode, "Step 2 - SearchListViewModel")
                }
            }
            is DataResultBooking.Loading -> {
                if (apiState.currentStep == 2) {
                    isLoading = true
                }
            }
        }
    }

    // Step 4: InitBookingViewModel observer - triggers GetBooking
    viewModel4.dataResult.observe(lifecycleOwner) { result ->
        Log.d("ReservationScreen", "Step 4 - InitBookingViewModel observer triggered - currentStep: ${apiState.currentStep}, currentDate: ${apiState.currentDate}")

        // Only process if this is for the current date being processed
        if (apiState.currentDate != selectedDate.value) {
            Log.d("ReservationScreen", "Step 4 - Ignoring result for old date: ${apiState.currentDate}, current: ${selectedDate.value}")
            return@observe
        }

        when (result) {
            is DataResultBooking.Success -> {
                if (apiState.currentStep == 4 && !apiState.initBookingFetched) {
                    Log.d("ReservationScreen", "Step 4 - InitBookingViewModel SUCCESS")
                    apiState = apiState.copy(initBookingFetched = true, currentStep = 5)

                    // Step 5: Call GetBooking
                    reservationKey.value?.let { key ->
                        Log.d("ReservationScreen", "Step 5 - Calling GetBooking with key: $key, date: ${selectedDate.value}")
                        getBookingViewModel.getBooking(key, selectedDate.value)
                    } ?: Log.e("ReservationScreen", "Step 5 - reservationKey is null!")
                }
            }
            is DataResultBooking.Failure -> {
                if (apiState.currentStep == 4) {
                    Log.e("ReservationScreen", "Step 4 - InitBookingViewModel FAILURE: ${result.errorCode}")
                    handleFailure(result.errorCode, "Step 4 - InitBookingViewModel")
                }
            }
            is DataResultBooking.Loading -> {
                if (apiState.currentStep == 4) {
                    isLoading = true
                }
            }
        }
    }

    // Step 5: GetBookingViewModel observer - final step
    getBookingViewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        Log.d("ReservationScreen", "Step 5 - GetBookingViewModel observer triggered - currentStep: ${apiState.currentStep}, currentDate: ${apiState.currentDate}")

        // Only process if this is for the current date being processed
        if (apiState.currentDate != selectedDate.value) {
            Log.d("ReservationScreen", "Step 5 - Ignoring result for old date: ${apiState.currentDate}, current: ${selectedDate.value}")
            return@observe
        }

        when (result) {
            is DataResultBooking.Success -> {
                if (apiState.currentStep == 5 && !apiState.bookingDataFetched) {
                    Log.d("ReservationScreen", "Step 5 - GetBookingViewModel SUCCESS")
                    apiState = apiState.copy(bookingDataFetched = true, currentStep = 6)
                    isLoading = false

                    val bookingResponses = result.data as? List<GetBookingResponseDTO>

                    if (bookingResponses != null && bookingResponses.isNotEmpty()) {
                        val plannings = bookingResponses.flatMap { it.plannings }
                        hasAvailableReservations = plannings.isNotEmpty()
                        hasNoData = !hasAvailableReservations
                        Log.d("ReservationScreen", "Step 5 - Found ${plannings.size} plannings for date: ${selectedDate.value}")
                    } else {
                        hasNoData = true
                        hasAvailableReservations = false
                        Log.d("ReservationScreen", "Step 5 - No booking responses found for date: ${selectedDate.value}")
                    }
                }
            }
            is DataResultBooking.Failure -> {
                if (apiState.currentStep == 5) {
                    Log.e("ReservationScreen", "Step 5 - GetBookingViewModel FAILURE: ${result.errorCode}")
                    handleFailure(result.errorCode, "Step 5 - GetBookingViewModel")
                    hasNoData = true
                    hasAvailableReservations = false
                }
            }
            is DataResultBooking.Loading -> {
                if (apiState.currentStep == 5) {
                    isLoading = true
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(
                isSelected = !showPaymentSection,
                title = "CHOISIR UN CRÉNEAU",
                icon = painterResource(id = R.drawable.calendre),
                onClick = { showPaymentSection = false },
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        DaySelectorWithArrows(
            selectedDate = selectedDate.value,
            onDateSelected = { newDate ->
                selectedDate.value = newDate
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (hasNoData) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pas des heures disponibles selon vos critères de recherche",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }

    if (!showPaymentSection) {
        Spacer(modifier = Modifier.height(10.dp))

        if (hasAvailableReservations) {
            ReservationOptions(
                onReservationSelected = { selectedReservation.value = it },
                key = reservationKey.value,
                viewModel = getBookingViewModel,
                navController = navController,
                selectedDate = selectedDate.value,
                selectedTimeSlot = selectedTimeSlot.value,
                paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                getReservationViewModel = getReservationViewModel
            )
        }
    }
}

@Composable
fun TabItem(
    isSelected: Boolean,
    title: String,
    icon: Painter,
    onClick: () -> Unit,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clickable { onClick() }
            .background(
                if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    navController.navigate("main_screen")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (isSelected) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.Black else Color.Gray,
                textAlign = TextAlign.Start
            )
        }

        if (isSelected) {
            Divider(
                color = Color(0xFF0054D8),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun ReservationSummary(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    selectedExtras: List<Triple<String, String, Int>>,
    amountSelected: Pair<Double, String>?,
    onTotalAmountCalculated: (Double) -> Unit,
    price: String,
    time: String,
    navController: NavController,
    adjustedAmount: Double,
    adjustedSharedExtrasAmount: Double,
    totalSharedExtrasCost: Double,
    totalExtrasCost: Double
) {

    Log.d("totalExtrCost","$totalExtrasCost")

    val totalAmountSelected = adjustedAmount + totalExtrasCost
    onTotalAmountCalculated(totalAmountSelected)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Détails Réservation",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ReservationDetailRow(label = "Espace", value = selectedReservation.name)
        ReservationDetailRow(label = "Prix", value = "${selectedReservation.price} DT"  )
        ReservationDetailRow(
            label = "Date",
            value = DateTimeFormatter.ofPattern("EEE d MMM yyyy", Locale.FRENCH)
                .format(selectedDate)
                .replace(".", "")
                .replaceFirstChar { it.uppercaseChar() }
        )

        ReservationDetailRow(label = "Heure", value = selectedReservation.time)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = "${String.format("%.2f", adjustedAmount)} DT")
        selectedExtras.forEach { (name, price, _) ->
            ReservationDetailRow(label = "Extra: $name", value = price)
        }
        ReservationDetailRow(label = "Total", value = "${String.format("%.2f", totalAmountSelected)} DT")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK",color = Color(0xFF0054D8), fontWeight = FontWeight.Bold,)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler",color = Color(0xFF0054D8), fontWeight = FontWeight.Bold,)
            }
        }
    ) {
        Surface(
            color = Color.White, // Set the background color to white
            modifier = Modifier.fillMaxSize() // Ensure it takes up the full space
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = remember { mutableStateOf<Long?>(null) }
    val selectedDate = datePickerState.value
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val maxSelectableDate = today + (21 * 24 * 60 * 60 * 1000)
    val calendar = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    calendar.set(Calendar.MONTH, currentMonth)
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val frenchMonths =
        listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            modifier = Modifier
                .heightIn(min = 10.dp, max = 600.dp)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 10.dp, max = 500.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (currentMonth == 0) {
                            currentMonth = 11
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Mois précédent",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "${frenchMonths[currentMonth]} $currentYear",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = {
                        if (currentMonth == 11) {
                            currentMonth = 0
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Mois suivant",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM").forEach {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    items(startDayOfWeek - 1) {
                        Box(modifier = Modifier.size(40.dp))
                    }

                    items(totalDaysInMonth) { day ->
                        val normalizedCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                            set(Calendar.DAY_OF_MONTH, day + 1)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val dateMillis = normalizedCalendar.timeInMillis

                        val isSelected = selectedDate?.let {
                            val selectedCalendar =
                                Calendar.getInstance().apply { timeInMillis = it }
                            selectedCalendar.get(Calendar.YEAR) == normalizedCalendar.get(
                                Calendar.YEAR
                            ) &&
                                    selectedCalendar.get(Calendar.MONTH) == normalizedCalendar.get(
                                Calendar.MONTH
                            ) &&
                                    selectedCalendar.get(Calendar.DAY_OF_MONTH) == normalizedCalendar.get(
                                Calendar.DAY_OF_MONTH
                            )
                        } ?: false

                        val isDisabled = dateMillis < today || dateMillis > maxSelectableDate

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    when {
                                        isSelected -> Color(0xFF0054D8)
                                        else -> Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                                .clickable(enabled = !isDisabled) {
                                    datePickerState.value = dateMillis
                                }
                        ) {
                            Text(
                                text = (day + 1).toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = when {
                                        isSelected -> Color.White
                                        isDisabled -> Color.Gray
                                        else -> Color.Black
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color.White)
                    ) {
                        Text("Annuler", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(selectedDate)
                            onDismiss()
                        },
                        modifier = Modifier.background(Color.White)
                    ) {
                        Text("OK", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


@Composable
fun DaySelectorWithArrows(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH)
    val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH)
    val currentDate = ZonedDateTime.now(ZoneId.of("Africa/Tunis")).toLocalDate()
    val finalSelectedDate = selectedDate ?: currentDate
    val startOfWeek = finalSelectedDate.with(DayOfWeek.MONDAY)
    val daysInWeek = List(7) { startOfWeek.plusDays(it.toLong()) }
    val maxFutureDate = currentDate.plusWeeks(3)
    val minPastDate = currentDate
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)
    val listState = rememberLazyListState()
    var showCalendarDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = monthYearFormatter.format(finalSelectedDate).uppercase(Locale.FRENCH),
            color = Color(0xFF0054D8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val newDate = finalSelectedDate.minusWeeks(1)
                    if (newDate >= minPastDate) {
                        onDateSelected(newDate)
                    }
                },
                enabled = finalSelectedDate > minPastDate
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous week", tint = Color.Gray)
            }

            Row(
                modifier = Modifier
                    .clickable { onDateSelected(currentDate) }
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AUJOURD'HUI",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0054D8),
                    fontSize = 12.sp,
                )
                Icon(
                    Icons.Filled.CalendarMonth,
                    contentDescription = "Today",
                    tint = Color(0xFF0054D8),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                        .clickable { showCalendarDialog = true }
                )
            }
            IconButton(
                onClick = {
                    val newDate = finalSelectedDate.plusWeeks(1)
                    if (newDate <= maxFutureDate) {
                        onDateSelected(newDate)
                    }
                },
                enabled = finalSelectedDate < maxFutureDate
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next week", tint = Color.Gray)
            }
        }

        if (showCalendarDialog) {
            CustomDatePickerModal(
                onDateSelected = { millis ->
                    millis?.let { onDateSelected(LocalDate.ofEpochDay(it / 86400000)) }
                    showCalendarDialog = false
                },
                onDismiss = { showCalendarDialog = false }
            )
        }
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(daysInWeek) { day ->
                val isClickable = day >= currentDate
                val textColor = if (!isClickable) Color.LightGray else if (day == finalSelectedDate) Color.White else Color.Black
                val backgroundColor = if (day == finalSelectedDate) Color(0xFF0054D8) else Color.White
                val borderColor = if (day == finalSelectedDate) Color.Transparent else Color.Gray

                Column(
                    modifier = Modifier
                        .then(
                            if (isClickable) Modifier.clickable { onDateSelected(day) }
                            else Modifier
                        )
                        .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 6.dp, horizontal = 6.dp)
                        .width(35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = dateFormatter.format(day),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = monthFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}



