package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileByIdViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetStatusesViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.GetStatusesResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SummaryScreen(
    viewModel: GetReservationViewModel = hiltViewModel(),
    viewModel1: GetProfileByIdViewModel = hiltViewModel(),
    viewModel2: GetStatusesViewModel = hiltViewModel(),
    navController: NavController,
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var filterByName by remember { mutableStateOf(false) }
    var nameFilterText by remember { mutableStateOf("") }
    val reservations = remember { mutableStateOf<List<GetReservationResponse>>(emptyList()) }
    val statuses = remember { mutableStateOf<List<GetStatusesResponse>>(emptyList()) }
    var selectedReservation by remember { mutableStateOf<GetReservationResponse?>(null) }
    var selectedReservationProfile by remember { mutableStateOf<GetReservationIDResponse?>(null) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val reservationData by viewModel.ReservationsData.observeAsState()
    val profilesData by viewModel1.profilesData.observeAsState()
    val reservationStatuses by viewModel2.ReservationsData1.observeAsState()
    var showNameFilterPopup by remember { mutableStateOf(false) }
    var filterByType by remember { mutableStateOf(false) }
    var selectedStatusType by remember { mutableStateOf("") }
    var showTypeFilterPopup by remember { mutableStateOf(false) }
    val isLoading = reservationData == null || reservationData is DataResultBooking.Loading
    var sortByReference by remember { mutableStateOf(false) }
    var showSortPopup by remember { mutableStateOf(false) }
    var filterByDate by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var referenceFilterText by remember { mutableStateOf("") }
    var filterByReference by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.GetReservation()
    }

    LaunchedEffect(reservationData) {
        if (reservationData is DataResultBooking.Success) {
            reservations.value = (reservationData as DataResultBooking.Success<List<GetReservationResponse>>).data
                .sortedByDescending {
                    LocalDate.parse(it.bookingDate, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE))
                }
        }
    }

    LaunchedEffect(profilesData) {
        if (profilesData is DataResultBooking.Success) {
            selectedReservationProfile =
                (profilesData as DataResultBooking.Success<GetReservationIDResponse>).data
        }
    }

    LaunchedEffect(reservationStatuses) {
        if (reservationStatuses is DataResultBooking.Success) {
            statuses.value = (reservationStatuses as DataResultBooking.Success<List<GetStatusesResponse>>).data
        }
    }


    val filteredReservations = remember(
        selectedDate,
        reservations.value,
        filterByName,
        nameFilterText,
        filterByType,
        selectedStatusType,
        sortByReference,
        filterByReference,
        referenceFilterText
    ) {
        var filteredList = reservations.value

        selectedDate?.let { dateMillis ->
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE)
            val selectedFormattedDate = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(formatter)

            filteredList = filteredList.filter { it.bookingDate == selectedFormattedDate }
        }

        if (filterByName) {
            filteredList = filteredList.filter {
                it.establishmentName.contains(nameFilterText, ignoreCase = true)
            }
        }
        if (filterByType && selectedStatusType.isNotEmpty()) {
            filteredList = filteredList.filter {
                it.bookingStatusName.equals(selectedStatusType, ignoreCase = true)
            }
        }

        if (filterByReference) {
            filteredList = filteredList.filter {
                it.reference?.contains(referenceFilterText, ignoreCase = true) ?: false
            }
        }

        filteredList
    }


    ModalBottomSheetLayout(
        sheetContent = {
            selectedReservation?.let { reservation ->
                selectedReservationProfile?.let { profile ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.87f)
                    ) {
                        ReservationCard1(reservation, profile, bookingStatusCode = reservation.bookingStatusCode)
                    }
                }
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Mes réservations",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFFF5F7F9), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE0E3E7), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    OutlinedButton(onClick = { showFilterMenu = !showFilterMenu }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filtrer", color = Color.Gray)
                    }
                    DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
                        DropdownMenuItem(onClick = {
                            filterByName = false
                            nameFilterText = ""
                            filterByType = false
                            selectedStatusType = ""
                            filterByReference = false
                            referenceFilterText = ""
                            selectedDate = null

                            showFilterMenu = false
                            showDatePicker = true
                        }) {
                            Text("Par Date")
                        }

                        DropdownMenuItem(onClick = {
                            filterByType = false
                            selectedStatusType = ""
                            filterByReference = false
                            referenceFilterText = ""
                            selectedDate = null

                            filterByName = true
                            showFilterMenu = false
                            showNameFilterPopup = true
                        }) { Text("Par Nom ") }
                        DropdownMenuItem(onClick = {
                            filterByName = false
                            nameFilterText = ""
                            filterByReference = false
                            referenceFilterText = ""
                            selectedDate = null

                            filterByType = true
                            showFilterMenu = false
                            showTypeFilterPopup = true
                        }) {
                            Text("Par État")
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.1f))

                Divider(color = Color.LightGray, modifier = Modifier.height(24.dp).width(1.dp))

                Spacer(modifier = Modifier.weight(0.09f))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            focusManager.clearFocus()
                        }
                ) {
                    if (referenceFilterText.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Référence de réservation",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    BasicTextField(
                        value = referenceFilterText,
                        onValueChange = {
                            referenceFilterText = it
                            filterByReference = it.isNotEmpty()
                        },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .offset(x = -11.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(start = 5.dp),

                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }
                            }
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))


            if (showSortPopup) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .shadow(4.dp)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            referenceFilterText = ""
                            showSortPopup = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = "Filtrer par référence",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        TextField(
                            value = referenceFilterText,
                            onValueChange = { referenceFilterText = it },
                            label = { Text("Référence de réservation") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedLabelColor = Color.LightGray,
                                unfocusedLabelColor = Color.LightGray,
                                cursorColor = Color.Black,
                                textColor = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            filterByReference = referenceFilterText.isNotEmpty()
                            showSortPopup = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0066CC), shape = RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0066CC))
                    ) {
                        Text(
                            text = "Filtrer",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            if (showDatePicker) {
                CustomDatePickerModal1(
                    onDateSelected = { dateMillis ->
                        selectedDate = dateMillis
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            if (showNameFilterPopup) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .shadow(4.dp)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            nameFilterText = ""
                            showNameFilterPopup = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = "Filtrer par nom d'espace",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        TextField(
                            value = nameFilterText,
                            onValueChange = { nameFilterText = it },
                            label = { Text("Nom d'établissement") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedLabelColor = Color.LightGray,
                                unfocusedLabelColor = Color.LightGray,
                                cursorColor = Color.Black,
                                textColor = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            filterByName = nameFilterText.isNotEmpty()
                            showNameFilterPopup = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0066CC), shape = RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0066CC))
                    ) {
                        Text(
                            text = "Filtrer",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }


            if (showTypeFilterPopup) {
                val statusOptions = listOf("Toutes Réservations", "Confirmée", "En attente de paiement")

                AlertDialog(
                    onDismissRequest = { showTypeFilterPopup = false },
                    title = {
                        Text(
                            text = "Filtrer par état",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    },
                    text = {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            statusOptions.forEach { status ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedStatusType = status
                                            filterByType = status != "Toutes Réservations"
                                        }
                                        .padding(8.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedStatusType.equals(status, ignoreCase = true),
                                        onClick = {
                                            selectedStatusType = status
                                            filterByType = status != "Toutes Réservations"
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0066CC))
                                    )
                                    Text(
                                        text = status,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showTypeFilterPopup = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0066CC), shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "OK",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                )
            }


            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                filteredReservations.forEach { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onClick = {
                            selectedReservation = reservation
                            viewModel1.fetchProfileById(reservation.id)
                            coroutineScope.launch { sheetState.show() }
                        },
                        viewModel1 = viewModel1,
                        bookingStatusCode = reservation.bookingStatusCode,
                        onShowDialog = { showDialog = true },
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showDialog) {
        selectedReservation?.let { reservation ->
            selectedReservationProfile?.let { profile ->
                Dialog(onDismissRequest = { showDialog = false }) {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val dialogHeight = (screenHeight * 0.85f)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dialogHeight)
                            .padding(36.dp)
                    ) {
                        ReservationCard1(reservation, profile, bookingStatusCode = reservation.bookingStatusCode)
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationCard(
    reservation: GetReservationResponse,
    onClick: () -> Unit,
    viewModel1: GetProfileByIdViewModel,
    bookingStatusCode: String,
    onShowDialog: () -> Unit,
    viewModel3: PrivateExtrasViewModel = hiltViewModel(),
    navController: NavController,
    viewModel2: GetProfileByIdViewModel = hiltViewModel(),
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    viewModel5: PartnerPayViewModel = hiltViewModel(),

    ) {


    val bookingDate = reservation.bookingDate
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE)
    var selectedReservation by remember { mutableStateOf<GetReservationResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var selectedReservation1 by remember { mutableStateOf<GetReservationIDResponse?>(null) }
    val date = LocalDate.parse(bookingDate, formatter)
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRANCE).replaceFirstChar { it.uppercaseChar() }
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale.FRANCE).replaceFirstChar { it.uppercaseChar() }
    val formattedDate = "$dayOfWeek ${date.dayOfMonth} $month ${date.year}"
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val profilesData by viewModel1.profilesData.observeAsState()
    val partnerPayResponse by viewModel5.partnerPayResponse.observeAsState()

    LaunchedEffect(partnerPayResponse) {
        partnerPayResponse?.let { response ->
        }
    }

    LaunchedEffect(profilesData) {
        if (profilesData is DataResultBooking.Success) {
            selectedReservation1 =
                (profilesData as DataResultBooking.Success<GetReservationIDResponse>).data
        }
    }


    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reservation.establishmentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                val formattedAmount = if (reservation.localAmount == 0.0) {
                    "0"
                } else {
                    if (reservation.token.isNullOrEmpty()) {
                        String.format("%.2f", reservation.localAmount)
                    } else {
                        String.format("%.0f", reservation.localAmount)
                    }
                }
                val amountText = when {
                    reservation.token.isNullOrEmpty() -> "$formattedAmount DT"
                    else -> "$formattedAmount CREDIT"
                }

                Text(
                    text = amountText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

            }
            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(
                    text = formattedDate,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${reservation.fromStrTime} - ${reservation.toStrTime}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = "${reservation.reference} ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = if (reservation.bookingStatusName == "erreur de paiement") "en attente de paiement" else reservation.bookingStatusName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(when (reservation.bookingStatusCode) {
                        "PAY" -> Color(0xFF4CAF50)
                        "WAIT" -> Color(0xFFFBC02D)
                        else -> if (reservation.bookingStatusName == "erreur de paiement") Color(0xFFFBC02D) else Color(0xFF4CAF50)
                    })
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(-6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (selectedReservation != null && selectedReservation1 != null) {
                    LaunchedEffect(sheetState) {
                        sheetState.show()
                    }
                }
                if (reservation.activePayment) {
                    IconButton(
                        onClick = {
                            viewModel2.fetchProfileById(reservation.id)
                            viewModel4.partnerPay(reservation.id)
                            navController.navigate("PartnerPaymentScreen/${reservation.id}")
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Payment Active",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                    }
                }
                IconButton(
                    onClick = {
                        if (!isLoading) {
                            isLoading = true
                            coroutineScope.launch {
                                viewModel1.fetchProfileById(reservation.id)
                                viewModel4.partnerPay(reservation.id)
                                delay(500)
                                isLoading = false
                                onClick()
                                sheetState.hide()
                                selectedReservation = null
                                selectedReservation1 = null
                            }
                        }
                    },
                    modifier = Modifier.size(24.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                    } else {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "View",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun ReservationCard1(
    reservation: GetReservationResponse,
    reservation1: GetReservationIDResponse,
    bookingStatusCode: String
) {
    val bookingDate = reservation.bookingDate
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE)
    val date = LocalDate.parse(bookingDate, formatter)
    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE d MMM yyyy", Locale.FRANCE))
    val formattedCreatedDate = date.format(DateTimeFormatter.ofPattern("EEEE d MMM yyyy", Locale.FRANCE))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = R.drawable.logopadelium),
                        contentDescription = "Establishment Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF0054D8), shape = CircleShape)
                            .padding(5.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = reservation.establishmentName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val formattedAmount = "%.0f".format(reservation.sellAmount)

                        Text(
                            text = "${formattedAmount } DT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )

                                Text(
                                    text = reservation.bookingStatusName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when (reservation.bookingStatusCode) {
                                                "PAY" -> Color(0xFF4CAF50)
                                                "WAIT" -> Color(0xFFFBC02D)
                                                else -> Color(0xFF4CAF50)
                                            }
                                        )
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = formattedDate,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Text(
                                    text = ", ${reservation.fromStrTime} - ${reservation.toStrTime}",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }


        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Payé par",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    reservation1.bookingUsersPaymentListDTO.forEach { user ->
                        val userName =
                            if (user.userFirstName == null && user.userLastName == null) "Invité" else "${user.userFirstName} ${user.userLastName}".trim()
                        val paymentStatus = when (user.bookingUsersPaymentStatusCode) {
                            "PAY" -> "Confirmée"
                            "WAIT" -> "En attente de paiement"
                            else -> "Inconnu"
                        }
                        val paymentColor =
                            if (user.bookingUsersPaymentStatusCode == "PAY") Color(0xFF4CAF50) else Color(0xFFFFC107)

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(userName, fontSize = 20.sp, color = Color.Gray)

                            Column(horizontalAlignment = Alignment.End) {
                                if (user.paymentMode == "CREDIT") {
                                    Text(
                                        "${user.amountstr} DT",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else if (user.paymentMode == "PAIMENT EN LIGNE") {
                                    val amountWithoutDecimal = user.amountstr.toDoubleOrNull()?.toInt()

                                    Text(
                                        "${amountWithoutDecimal} Crédits",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = paymentStatus,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = paymentColor
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Réservation",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    TableRow(label = "Référence", value = reservation.reference, isBold = true)
                    Divider()
                    TableRow(label = "Créé le", value = formattedCreatedDate, isBold = true)
                }
            }
        }
    }
}

@Composable
fun TableRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = Color.LightGray)
        Text(value, fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = Color.LightGray)
    }
}



@Preview
@Composable
fun PreviewReservationCard1() {
    val mockReservation = GetReservationResponse(
        id = 1L,
        from = "10:00",
        to = "11:30",
        annulationDate = "",
        sellAmount = 50.0,
        purchaseAmount = 0.0,
        numberOfPlayer = 4,
        reference = "123456",
        description = "Reservation de test",
        isRefundable = true,
        created = "",
        updated = "",
        createdBy = 1L,
        updatedBy = 1L,
        currencyFromId = 1L,
        currencyToId = 1L,
        bookingStatusId = 1L,
        establishmentId = 1L,
        userId = 1L,
        userLogin = "testuser",
        establishmentName = "Padel Club",
        bookingStatusName = "",
        userEmail = "user@example.com",
        currencyFromSymbol = "€",
        gainFromManager = 0.0,
        bookingStatusCode = "CONF",
        userPhone = "123456789",
        cancelBook = false,
        cancel = false,
        isonline = true,
        activityName = "Padel",
        cityName = "Paris",
        establishmentCode = "PAD123",
        localAmount = 50.0,
        reduction = 0,
        showcancel = false,
        showfeedBack = false,
        bookingDate = "22-01-2025",
        token = "",
        paymentError = false,
        paymentprog = false,
        amountToPay = 50.0,
        sobflousCode = "",
        couponId = 0L,
        isCoupon = false,
        couponValue = "",
        couponCode = "",
        establishmentPacksId = 0L,
        establishmentTypeCode = "",
        isConfirmed = true,
        isFromEvent = false,
        establishmentPacksFirstTitle = "",
        establishmentPacksSecondTitle = "",
        usersIds = listOf(),
        fromStr = "10:00",
        toStr = "11:30",
        fromStrTime = "10:00",
        toStrTime = "11:30",
        activePayment = false,
        isWaitForPay = false,
        bookingLabelId = 1L,
        bookingLabelName = "",
        bookingLabelColors = "",
        sharedExtrasIds = listOf(),
        privateExtrasIds = listOf(),
        privateExtrasLocalIds = mutableMapOf(),
        userFirstName = "John",
        userLastName = "Doe",
        extras = listOf(),
        numberOfPart = 1,
        createdStr = ""
    )
    val mockReservation1 = GetReservationIDResponse(
        id = 1L,
        from = "10:00",
        to = "11:30",
        //annulationDate = ,
        sellAmount = 50.0.toBigDecimal(),
        purchaseAmount = 0.0.toBigDecimal(),
        numberOfPlayer = 4,
        reference = "123456",
        description = "Reservation de test",
        isRefundable = true,
        created = "",
        updated = "",
        createdBy = 1L,
        updatedBy = 1L,
        currencyFromId = 1L,
        currencyToId = 1L,
        bookingStatusId = 1L,
        establishmentId = 1L,
        userId = 1L,
        userLogin = "testuser",
        establishmentName = "Padel Club",
        bookingStatusName = "",
        userEmail = "user@example.com",
        currencyFromSymbol = "€",
        gainFromManager = 0L,
        bookingStatusCode = "CONF",
        userPhone = "123456789",
        cancelBook = false,
        cancel = false,
        isonline = true,
        activityName = "Padel",
        cityName = "Paris",
        establishmentCode = "PAD123",
        localAmount = 50.0.toBigDecimal(),
        reduction = 0,
        showcancel = false,
        showfeedBack = false,
        bookingDate = "22-01-2025",
        token = "",
        paymentError = false,
        paymentprog = false,
        amountToPay = 50.0.toBigDecimal(),
        sobflousCode = "",
        couponId = 0L,
        isCoupon = false,
        couponValue = "",
        couponCode = "",
        establishmentPacksId = 0L,
        establishmentTypeCode = "",
        isConfirmed = true,
        isFromEvent = false,
        establishmentPacksFirstTitle = "",
        establishmentPacksSecondTitle = "",
        usersIds = listOf(),
        fromStr = "10:00",
        toStr = "11:30",
        fromStrTime = "10:00",
        toStrTime = "11:30",
        activePayment = false,
        isWaitForPay = false,
        bookingLabelId = 1L,
        bookingLabelName = "",
        bookingLabelColors = "",
        sharedExtrasIds = listOf(),
        privateExtrasIds = listOf(),
        privateExtrasLocalIds = mutableMapOf(),
        userFirstName = "John",
        userLastName = "Doe",
        extras = listOf(),
        numberOfPart = 1,
        createdStr = "",
        bookingUsersPaymentListDTO = listOf(),
        annulationDate = Instant.now(),

        )
         val mockReservation2 = GetStatusesResponse(
        id = 1L,
        name = "10:00",
        code = "11:30",
        created = "123456",
        updated = "123456",
        isshow = false
         )
    ReservationCard1(reservation = mockReservation, reservation1 = mockReservation1, bookingStatusCode ="",
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerModal1(
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
    val calendar = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    calendar.set(Calendar.MONTH, currentMonth)
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val frenchMonths = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc")

    Dialog(onDismissRequest = onDismiss) {
        androidx.compose.material3.Surface(
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
                    androidx.compose.material3.IconButton(onClick = {
                        if (currentMonth == 0) {
                            currentMonth = 11
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    }) {
                        androidx.compose.material3.Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Mois précédent",
                            tint = Color.Black
                        )
                    }
                    androidx.compose.material3.Text(
                        text = "${frenchMonths[currentMonth]} $currentYear",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    androidx.compose.material3.IconButton(onClick = {
                        if (currentMonth == 11) {
                            currentMonth = 0
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }) {
                        androidx.compose.material3.Icon(
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
                        androidx.compose.material3.Text(
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
                                .clickable() {
                                    datePickerState.value = dateMillis
                                }
                        ) {
                            androidx.compose.material3.Text(
                                text = (day + 1).toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = when {
                                        isSelected -> Color.White
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
                    androidx.compose.material3.TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color.White)
                    ) {
                        androidx.compose.material3.Text(
                            "Annuler",
                            color = Color(0xFF0054D8),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onDateSelected(selectedDate)
                            onDismiss()
                        },
                        modifier = Modifier.background(Color.White)
                    ) {
                        androidx.compose.material3.Text(
                            "OK",
                            color = Color(0xFF0054D8),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
