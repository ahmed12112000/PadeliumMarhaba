package com.nevaDev.padeliummarhaba.ui.views


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SharedExtrasViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.FindTermsResponse
import com.padelium.domain.dto.PrivateExtrasResponse
import com.padelium.domain.dto.SharedExtrasResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.math.RoundingMode

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReservationDetailsCard(
    selectedExtras: MutableList<Triple<String, String, Int>>,
    onExtrasUpdate: (List<Triple<String, String, Int>>, Double) -> Unit,
    selectedReservation: ReservationOption,
    viewModel2: ExtrasViewModel = hiltViewModel(),
    viewModel3: SharedExtrasViewModel = hiltViewModel(),
    viewModel4: PrivateExtrasViewModel = hiltViewModel(),
    amountSelected: Pair<Double, String>?,
    onAdjustedAmountUpdated: (Double, Double) -> Unit,
    onPartsSelected: (Int) -> Unit,
    viewModel1: SharedViewModel,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
) {
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    var partnerName by remember { mutableStateOf("") }
    var partsDropdownExpanded by remember { mutableStateOf(false) }
    var partnerSearchDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(partnerName) {
        debounceJob?.cancel()
        debounceJob = launch {
            delay(500)
            if (partnerName.isNotEmpty()) {
                val requestBody: RequestBody = partnerName.toRequestBody("text/plain".toMediaType())
                findTermsViewModel.findTerms(requestBody)
                partnerSearchDropdownExpanded = true
            } else {
                partnerSearchDropdownExpanded = false
            }
        }
    }
    val playerFullNames by findTermsViewModel.playerFullNames.observeAsState(emptyList())

    // Observe players state
    val playersState by findTermsViewModel.players.observeAsState(initial = DataResult.Loading)
    val players = remember(playersState) {
        (playersState as? DataResult.Success)?.data as? List<FindTermsResponse> ?: emptyList()
    }
    Log.d("ReservationDetailsCard", "Player Full Names: $playerFullNames")

    val reservationPrice = try {
        val priceString = selectedReservation.price.replace("[^\\d.,]".toRegex(), "")
        priceString.replace(",", ".").toDoubleOrNull() ?: 0.0
    } catch (e: Exception) {
        Log.e("ReservationSummary", "Error parsing reservation price: ${selectedReservation.price}", e)
        0.0
    }

    Log.d("ReservationDetailsCard", "reservationPrice: $reservationPrice")

    var extrasEnabled by remember { mutableStateOf(false) }
    var totalExtrasCost by remember { mutableStateOf(0.0) }

    // Observe selected parts from ViewModel
    val selectedParts by viewModel1.selectedParts.collectAsState()

    // Fetch extra data
    val extrasState by viewModel2.extrasState.observeAsState()
    val sharedExtrasState by viewModel3.extrasState1.observeAsState()

    LaunchedEffect(viewModel3) {
        viewModel3.SharedExtras()
    }

    LaunchedEffect(viewModel2) {
        viewModel2.Extras()
        viewModel3.SharedExtras()
        viewModel4.PrivateExtras()
    }

    // Calculate total reservation amount including extras
    val totalAmountSelected = remember(totalExtrasCost) {
        val reservationAmount = selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        reservationAmount + totalExtrasCost
    }

    // Calculate shared extras cost based on selected extras
    val totalSharedExtrasCost = remember(selectedExtras) {
        selectedExtras.sumOf { it.second.toDouble() }
    }

    // Adjusted amount calculation based on selected parts
    val adjustedAmount = remember(selectedParts, amountSelected) {
        val baseAmount = amountSelected?.first ?: 0.0
        when (selectedParts) {
            1 -> (baseAmount / 4.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            2 -> (baseAmount / 2.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            3 -> ((baseAmount / 4.0) * 3.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            4 -> baseAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            else -> baseAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        }.also { amount ->
            Log.d("ReservationDetailsCard", "Adjusted Amount for $selectedParts parts: $amount")
        }
    }

    val adjustedSharedExtrasAmount = remember(selectedParts, totalSharedExtrasCost) {
        viewModel3.calculateAdjustedSharedExtras(totalSharedExtrasCost, selectedParts)
    }.also { amount ->
        Log.d("ReservationDetailsCard", "Adjusted Shared Extras Amount for $selectedParts parts: $amount")
    }

    LaunchedEffect(adjustedAmount, adjustedSharedExtrasAmount) {
        onAdjustedAmountUpdated(adjustedAmount, adjustedSharedExtrasAmount)
        Log.d("AMIR", "Adjusted Amount: $adjustedAmount, Adjusted Shared Extras Amount: $adjustedSharedExtrasAmount")
    }

    LaunchedEffect(partnerName) {
        debounceJob?.cancel() // Cancel the previous job if a new one starts
        debounceJob = launch {
            delay(500) // Adjust the delay time as needed
            if (partnerName.isNotEmpty()) {
                val requestBody: RequestBody = partnerName.toRequestBody("text/plain".toMediaType())
                findTermsViewModel.findTerms(requestBody)
                partnerSearchDropdownExpanded = true
            } else {
                partnerSearchDropdownExpanded = false
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Dropdown for selecting parts
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
            ) {
                // Text
                Text(
                    text = "Je veux payer pour",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp) // Add space between text and dropdown
                )

                // Dropdown
                ExposedDropdownMenuBox(
                    expanded = partsDropdownExpanded,
                    onExpandedChange = { partsDropdownExpanded = !partsDropdownExpanded }
                ) {
                    TextField(
                        value = viewModel1.selectedParts.value.toString(),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .width(50.dp) // Adjusted width for dropdown
                            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 22.sp, // Make the number bigger
                            fontWeight = FontWeight.Bold
                        ),
                        placeholder = { Text("Select") }
                    )
                    ExposedDropdownMenu(
                        expanded = partsDropdownExpanded,
                        onDismissRequest = { partsDropdownExpanded = false }
                    ) {
                        listOf(1, 2, 3, 4).forEach { option ->
                            DropdownMenuItem(onClick = {
                                viewModel1.updateSelectedParts(option)
                                partsDropdownExpanded = false
                            }) {
                                Text(text = option.toString(), fontSize = 16.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                val selectedPartsValue = viewModel1.selectedParts.collectAsState().value

                Text(
                    text = if (selectedPartsValue == 1) "Part ?" else "Parts ?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel1.selectedParts.collectAsState().value in 1..2) {
                Text(
                    text = "Sélectionnez vos partenaires",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else if (viewModel1.selectedParts.collectAsState().value == 3) {
                Text(
                    text = "Sélectionnez votre partenaire",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            // Only show this section if selectedParts is not 4
            if (viewModel1.selectedParts.collectAsState().value != 4) {
                Column {
                    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
                    val partners = remember { mutableStateListOf<Pair<String, Long?>>() }
                    val coroutineScope = rememberCoroutineScope()

                    if (selectedParts in 1..3) {
                        when (selectedParts) {
                            1 -> {
                                for (i in 0 until 3) {
                                    PartnerField(
                                        label = "Partenaire ${i + 1}",
                                        value = partners.getOrNull(i)?.first ?: "",
                                        onValueChange = { text ->
                                            if (partners.size > i) {
                                                partners[i] = Pair(text, partners[i].second)
                                            } else {
                                                partners.add(Pair(text, null))
                                            }
                                        },
                                        selectedPlayers = selectedPlayers,
                                        findTermsViewModel = findTermsViewModel,
                                        playerFullNames = playerFullNames,
                                        onPlayerSelected = { selectedName, selectedId ->
                                            if (partners.size > i) {
                                                partners[i] = Pair(selectedName, selectedId)
                                            } else {
                                                partners.add(Pair(selectedName, selectedId))
                                            }
                                            if (!selectedPlayers.contains(selectedId)) {
                                                selectedPlayers.add(selectedId)
                                            }
                                        }
                                    )
                                }
                            }

                            2 -> {
                                for (i in 0 until 2) {
                                    PartnerField(
                                        label = "Partenaire ${i + 1}",
                                        value = partners.getOrNull(i)?.first ?: "",
                                        onValueChange = { text ->
                                            if (partners.size > i) {
                                                partners[i] = Pair(text, partners[i].second)
                                            } else {
                                                partners.add(Pair(text, null))
                                            }
                                        },
                                        selectedPlayers = selectedPlayers,
                                        findTermsViewModel = findTermsViewModel,
                                        playerFullNames = playerFullNames,
                                        onPlayerSelected = { selectedName, selectedId ->
                                            if (partners.size > i) {
                                                partners[i] = Pair(selectedName, selectedId)
                                            } else {
                                                partners.add(Pair(selectedName, selectedId))
                                            }
                                            if (!selectedPlayers.contains(selectedId)) {
                                                selectedPlayers.add(selectedId)
                                            }
                                        }
                                    )
                                }
                            }

                            3 -> {
                                PartnerField(
                                    label = "Partenaire",
                                    value = partners.getOrNull(0)?.first ?: "",
                                    onValueChange = { text ->
                                        if (partners.isEmpty()) {
                                            partners.add(Pair(text, null))
                                        } else {
                                            partners[0] = Pair(text, partners[0].second)
                                        }
                                    },
                                    selectedPlayers = selectedPlayers,
                                    findTermsViewModel = findTermsViewModel,
                                    playerFullNames = playerFullNames,
                                    onPlayerSelected = { selectedName, selectedId ->
                                        if (partners.getOrNull(0)?.first?.isNotEmpty() == true) {
                                            partners[0] = Pair(selectedName, selectedId)
                                        } else {
                                            partners.add(Pair(selectedName, selectedId))
                                        }
                                        if (!selectedPlayers.contains(selectedId)) {
                                            selectedPlayers.add(selectedId)
                                        }
                                    }
                                )
                            }
                        }
                        val userIds = selectedPlayers.joinToString(
                            prefix = "ID=[",
                            postfix = "]"
                        ) { it.toString() }
                        Log.d("Debug", "Current selectedPlayers list: $selectedPlayers")

                        Log.d("abdallah", "Final Selected Players IDs: $userIds")

                        LaunchedEffect(selectedPlayers) {
                            Log.d("AMANI", "Final Selected Players (in LaunchedEffect): $userIds")
                        }
                    }
                }
            }
        }
    }
}







@Composable
fun PartnerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    findTermsViewModel: FindTermsViewModel,
    selectedPlayers: MutableList<Long>,
    playerFullNames: List<String>,
    onPlayerSelected: (String, Long) -> Unit
) {
    val playersState by findTermsViewModel.players.observeAsState(initial = DataResult.Loading)
    var dropdownExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Track when the text changes
    var isTextChanged by remember { mutableStateOf(false) }

    // Manage the Dropdown visibility and focus behavior
    LaunchedEffect(value) {
        isTextChanged = value.isNotEmpty()
        dropdownExpanded = isTextChanged
    }

    // State to hold the position of the OutlinedTextField
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    Column {
        Box {
            OutlinedTextField(
                value = value,
                onValueChange = { text ->
                    onValueChange(text)
                    // Trigger search with the entered text
                    if (text.isNotEmpty()) {
                        val requestBody: RequestBody =
                            text.toRequestBody("text/plain".toMediaType())

                        // Fetch only 9 players for the first letter and progressively fetch more as letters are typed
                        if (text.length == 1) {
                            findTermsViewModel.findTerms(requestBody, limit = 9) // Fetch 9 players initially
                        } else {
                            findTermsViewModel.findTerms(requestBody) // Continue fetching results as letters are added
                        }

                        dropdownExpanded = true
                    } else {
                        dropdownExpanded = false
                    }
                    isTextChanged = text.isNotEmpty()
                },
                label = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)  // Ensure focus stays on the text field
                    .onFocusChanged { focusState ->
                        // This helps prevent dropdown from stealing focus
                        if (focusState.isFocused) {
                            dropdownExpanded = isTextChanged  // Only show dropdown if text is entered
                        }
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        textFieldSize = layoutCoordinates.size // Get the size of the OutlinedTextField
                    },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                )
            )

            // Only show the dropdown when there is text and the dropdownExpanded is true
            if (dropdownExpanded) {
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = {
                        dropdownExpanded = false
                        focusRequester.requestFocus() // Request focus back to the text field
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.TopStart) // Align the dropdown to the top start of the Box
                        .offset(y = with(LocalDensity.current) { textFieldSize.height.toDp() }) // Offset the dropdown below the OutlinedTextField
                ) {
                    when (playersState) {
                        is DataResult.Loading -> {
                            DropdownMenuItem(onClick = {}) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }

                        is DataResult.Success -> {
                            if (playerFullNames.isEmpty()) {
                                DropdownMenuItem(onClick = {}) {
                                    Text(text = "No results found", modifier = Modifier.padding(16.dp))
                                }
                            } else {
                                playerFullNames.forEach { fullName ->
                                    DropdownMenuItem(onClick = {
                                        val selectedPlayerData = findTermsViewModel.getPlayerByFullName(fullName)
                                        selectedPlayerData?.let { player ->
                                            val playerId = player.id
                                            if (!selectedPlayers.contains(playerId)) {
                                                selectedPlayers.add(playerId)
                                                onPlayerSelected(player.fullName, playerId) // Pass both the selected name and ID
                                                Log.d("MAYSSA", "Selected Player Added: ID = ${playerId}")
                                            }
                                        }
                                        dropdownExpanded = false
                                        focusManager.clearFocus()  // Remove focus from text field when a player is selected
                                    }) {
                                        Text(text = fullName)
                                    }
                                }
                            }
                        }

                        is DataResult.Failure -> {
                            DropdownMenuItem(onClick = {}) {
                                Text(
                                    text = "Error: ${(playersState as DataResult.Failure).errorMessage}",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {
                            DropdownMenuItem(onClick = {}) {
                                Text(
                                    text = "Start typing to search for partners.",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



/*
        Text(
            text = "Selected Players: ${
                selectedPlayers.joinToString { "${it.first} (${it.second})" }
            }",
            modifier = Modifier.padding(top = 8.dp)
        )


    }
}

 */

@Composable
fun ExtrasSection(
    onExtrasUpdate: (List<Triple<String, String, Int>>, Double) -> Unit,
    viewModel3: SharedExtrasViewModel = hiltViewModel(),
    viewModel4: PrivateExtrasViewModel = hiltViewModel(),
    selectedParts: String,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
) {
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val sharedExtrasState by viewModel3.extrasState1.observeAsState()
    val privateExtrasState by viewModel4.extrasState2.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }

    val sharedList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }
    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }

    LaunchedEffect(viewModel3, viewModel4) {
        viewModel3.SharedExtras()
        viewModel4.PrivateExtras()
    }

    // Calculate totalExtrasCost dynamically based on selectedExtras
    val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() }

    // Notify ReservationSummary of the total amount

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-8).dp), // Reduced offset for tighter spacing
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
                Spacer(modifier = Modifier.height(2.dp)) // Reduced height for tighter spacing

                when {
                    sharedExtrasState is DataResult.Loading || privateExtrasState is DataResult.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    sharedExtrasState is DataResult.Success && privateExtrasState is DataResult.Success -> {
                        val sharedExtrasList =
                            (sharedExtrasState as DataResult.Success).data as? List<SharedExtrasResponse>
                        val privateExtrasList =
                            (privateExtrasState as DataResult.Success).data as? List<PrivateExtrasResponse>

                        // Reduced height for tighter spacing between cards
                        Spacer(modifier = Modifier.height(1.dp))

                        // Render private extras
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) { // Reduced spacing between items

                                    Text(
                                        text = "  Article(s) réserver à mon usage",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                privateExtrasList?.forEach { privateExtra ->
                                    val isPrivateExtraAdded =
                                        selectedExtras.any { it.first == privateExtra.name }
                                    ExtraItemCard(
                                        extra = privateExtra,
                                        isAdded = isPrivateExtraAdded,
                                        onAddClick = { extraPrice ->
                                            privateList.value.add(privateExtra.id)
                                            Log.d(
                                                "ExtrasSection",
                                                "Updated privateList: ${privateList.value}"
                                            )
                                            findTermsViewModel.updatePrivateExtras(privateList.value)

                                            selectedExtras += Triple(
                                                privateExtra.name,
                                                privateExtra.amount.toString(),
                                                privateExtra.currencyId.toInt()
                                            )
                                            onExtrasUpdate(selectedExtras, totalExtrasCost)
                                        },
                                        onRemoveClick = { extraPrice ->
                                            privateList.value.remove(privateExtra.id)
                                            Log.d(
                                                "ExtrasSection",
                                                "Updated privateList: ${privateList.value}"
                                            )
                                            findTermsViewModel.updatePrivateExtras(privateList.value)

                                            selectedExtras =
                                                selectedExtras.filterNot { it.first == privateExtra.name }
                                            onExtrasUpdate(selectedExtras, totalExtrasCost)
                                        }
                                    )

                            }
                            // Reduced height between cards
                            Spacer(modifier = Modifier.height(4.dp))

                            // Render shared extras

                                Column {
                                    Text(
                                        text = "  Article(s) partagé(s) entre tous les joueurs",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "  (Frais partagé)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                sharedExtrasList?.forEach { sharedExtra ->
                                    val isSharedExtraAdded =
                                        selectedExtras.any { it.first == sharedExtra.name }
                                    ExtraItemCard(
                                        extra = sharedExtra,
                                        isAdded = isSharedExtraAdded,
                                        onAddClick = { extraPrice ->
                                            sharedList.value.add(sharedExtra.id)
                                            Log.d(
                                                "ExtrasSection",
                                                "Updated sharedList: ${sharedList.value}"
                                            )
                                            findTermsViewModel.updateSharedExtras(sharedList.value)

                                            // Calculate the cost based on selected parts
                                            val sharedAmount = when (selectedParts.toInt()) {
                                                1 -> (extraPrice / 4.0).toBigDecimal()
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .toDouble() // Divided among 4
                                                2 -> (extraPrice / 2.0).toBigDecimal()
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .toDouble() // Divided among 2
                                                3 -> (extraPrice / 1.33).toBigDecimal()
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .toDouble() // Approximately divided among 3
                                                4 -> (extraPrice / 1.0).toBigDecimal()
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .toDouble() // No division
                                                else -> extraPrice.toBigDecimal()
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .toDouble() // Default case
                                            }

                                            selectedExtras += Triple(
                                                sharedExtra.name,
                                                sharedAmount.toString(),
                                                sharedExtra.currencyId.toInt()
                                            )
                                            onExtrasUpdate(selectedExtras, totalExtrasCost)
                                        },
                                        onRemoveClick = { extraPrice ->
                                            sharedList.value.remove(sharedExtra.id)
                                            Log.d(
                                                "ExtrasSection",
                                                "Updated sharedList: ${sharedList.value}"
                                            )
                                            findTermsViewModel.updateSharedExtras(sharedList.value)

                                            // Calculate the cost based on selected parts
                                            val sharedAmount = when (selectedParts.toInt()) {
                                                1 -> extraPrice / 4.0 // Divided among 4
                                                2 -> extraPrice / 2.0 // Divided among 2
                                                3 -> extraPrice / 1.33 // Approximately divided among 3
                                                4 -> extraPrice / 1.0 // No division
                                                else -> extraPrice // Default case
                                            }

                                            selectedExtras =
                                                selectedExtras.filterNot { it.first == sharedExtra.name }
                                            onExtrasUpdate(selectedExtras, totalExtrasCost)
                                        }
                                    )
                                }
                            }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Failed to load extras",
                                color = Color.Red
                            )

                    }
                }
            }
        }

}




@Composable
fun ReservationCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) { // Reduce inner padding
            Text(
                text = title,
                fontSize = 16.sp, // Reduce font size
                fontWeight = FontWeight.Medium // Use medium weight for a more subtle look
            )
            Spacer(modifier = Modifier.height(4.dp)) // Slightly larger spacer for visual separation
            content()
        }
    }
}
@Composable
fun ExtraItemCard(
    extra: Any,
    isAdded: Boolean,
    onAddClick: (Double) -> Unit,
    onRemoveClick: (Double) -> Unit
) {
    var addedState by remember { mutableStateOf(isAdded) }
    val pricePerPart = when (extra) {
        is SharedExtrasResponse -> extra.amount?.toDouble() ?: 0.0
        is PrivateExtrasResponse -> extra.amount?.toDouble() ?: 0.0
        else -> 0.0
    } / 4
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {

        val baseUrl = "http://141.94.246.248/"

        val imageUrl = when (extra) {
            is SharedExtrasResponse -> "$baseUrl${extra.picture}"
            is PrivateExtrasResponse -> "$baseUrl${extra.picture}"
            else -> ""
        }

        Log.d("ExtraItemCard", "Image URL: $imageUrl")

        val painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                error(R.drawable.star)
            }
        )

        Image(
            painter = painter,
            contentDescription = when (extra) {
                is SharedExtrasResponse -> extra.name
                is PrivateExtrasResponse -> extra.name
                else -> ""
            },
            modifier = Modifier
                .size(30.dp) // Adjust the size of the image
                .padding(start = 4.dp, top = 4.dp) // Add padding for positioning
                .offset(y = 4.dp) // Slightly move the image down
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = when (extra) {
                    is SharedExtrasResponse -> extra.name
                    is PrivateExtrasResponse -> extra.name
                    else -> ""
                },
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${when (extra) {
                    is SharedExtrasResponse -> extra.amount
                    is PrivateExtrasResponse -> extra.amount
                    else -> ""
                }} DT"

            )
        }

        IconButton(onClick = {
            val extraPrice = when (extra) {
                is SharedExtrasResponse -> extra.amount?.toDouble() ?: 0.0
                is PrivateExtrasResponse -> extra.amount?.toDouble() ?: 0.0
                else -> 0.0
            }

            if (addedState) {
                onRemoveClick(extraPrice)
            } else {
                onAddClick(extraPrice)
            }
            addedState = !addedState
        }) {
            Icon(
                painter = painterResource(
                    id = if (addedState) R.drawable.moins else R.drawable.plus
                ),
                contentDescription = if (addedState) "Remove" else "Add",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (addedState) Color.White else Color(0xFF0054D8),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        }
    }
}







