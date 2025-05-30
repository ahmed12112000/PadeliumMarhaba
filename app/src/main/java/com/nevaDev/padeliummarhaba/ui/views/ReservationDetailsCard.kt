package com.nevaDev.padeliummarhaba.ui.views


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.padelium.data.dto.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SharedExtrasViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
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
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    val selectedParts by viewModel1.selectedParts.collectAsState()

    LaunchedEffect(viewModel3) {
        viewModel3.SharedExtras()
    }

    LaunchedEffect(viewModel2) {
        viewModel2.Extras()
        viewModel3.SharedExtras()
        viewModel4.PrivateExtras()
    }

    val totalSharedExtrasCost = remember(selectedExtras) {
        selectedExtras.sumOf { it.second.toDouble() }
    }

    val adjustedAmount = remember(selectedParts, amountSelected) {
        val baseAmount = amountSelected?.first ?: 0.0
        when (selectedParts) {
            1 -> (baseAmount / 4.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            2 -> (baseAmount / 2.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            3 -> ((baseAmount / 4.0) * 3.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            4 -> baseAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
            else -> baseAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        }.also { amount ->
        }
    }

    val adjustedSharedExtrasAmount = remember(selectedParts, totalSharedExtrasCost) {
        viewModel3.calculateAdjustedSharedExtras(totalSharedExtrasCost, selectedParts)
    }.also { amount ->
    }

    LaunchedEffect(adjustedAmount, adjustedSharedExtrasAmount) {
        onAdjustedAmountUpdated(adjustedAmount, adjustedSharedExtrasAmount)
    }

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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        partsDropdownExpanded = false
                        partnerSearchDropdownExpanded = false
                    })
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Je veux payer pour",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = partsDropdownExpanded,
                    onExpandedChange = { partsDropdownExpanded = !partsDropdownExpanded }
                ) {
                    TextField(
                        value = viewModel1.selectedParts.value.toString(),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .width(50.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 22.sp,
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel1.selectedParts.collectAsState().value in 1..2) {
                Text(
                    text = "Sélectionnez vos partenaires(Optionnel)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else if (viewModel1.selectedParts.collectAsState().value == 3) {
                Text(
                    text = "Sélectionnez vos partenaires(Optionnel)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            // REPLACE THE ENTIRE SECTION BELOW WITH UpdatedPartnerManagement
            if (viewModel1.selectedParts.collectAsState().value != 4) {
                UpdatedPartnerManagement(
                    selectedParts = selectedParts,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames
                )
            }
        }
    }
}


// Fixed version of the partner selection logic
// Fixed version of the partner selection logic

// Fixed version of the partner selection logic
@Composable
fun PartnerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    findTermsViewModel: FindTermsViewModel,
    selectedPlayers: MutableList<Long>,
    playerFullNames: List<String>,
    onPlayerSelected: (String, Long) -> Unit,
    onRemovePlayer: () -> Unit,
    isDisabled: Boolean = false,
    selectedPlayerId: Long?,
    showSelectedPlayerCard: Boolean = true // New parameter to control card display
) {
    val playersState by findTermsViewModel.players.observeAsState(initial = DataResult.Loading)
    val shouldShowGuest by findTermsViewModel.shouldShowGuest.observeAsState(initial = false)
    var showDropdown by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) } // Track focus state
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { newText ->
                onValueChange(newText)
                showDropdown = newText.isNotEmpty()

                if (newText.length >= 1) {
                    val requestBody: RequestBody = newText.toRequestBody("text/plain".toMediaType())
                    findTermsViewModel.findTerms(requestBody, limit = 9)
                } else {
                    // Reset guest fallback when text is cleared
                    findTermsViewModel.resetGuestFallback()
                }
            },
            label = {
                Text(
                    // Show "Partenaire" when focused, otherwise show the original label
                    if (isFocused) "Partenaire" else label
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (!focusState.isFocused) {
                        showDropdown = false
                        keyboardController?.hide()
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { /* Allow taps on the text field itself */ }
                    )
                },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    showDropdown = false
                }
            )
        )

        // Dropdown for player suggestions
        if (showDropdown && selectedPlayerId == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                when (playersState) {
                    is DataResult.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is DataResult.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Show player suggestions if available
                            if (playerFullNames.isNotEmpty()) {
                                items(playerFullNames) { fullName ->
                                    DropdownPlayerItem(
                                        playerName = fullName,
                                        onPlayerClick = {
                                            val selectedPlayerData = findTermsViewModel.getPlayerByFullName(fullName)
                                            selectedPlayerData?.let { player ->
                                                val playerId = player.id
                                                if (!selectedPlayers.contains(playerId)) {
                                                    selectedPlayers.add(playerId)
                                                    onPlayerSelected(player.fullName, playerId)
                                                    showDropdown = false
                                                    focusManager.clearFocus()
                                                    keyboardController?.hide()
                                                }
                                            }
                                        }
                                    )
                                }
                            }

                            // Show guest option if no players found and 3 seconds have passed
                            if (playerFullNames.isEmpty() && shouldShowGuest && value.isNotEmpty()) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                // Select as guest (use negative ID to distinguish from real players)
                                                val guestId = -System.currentTimeMillis() // Unique negative ID
                                                onPlayerSelected("Invité", guestId)
                                                showDropdown = false
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PersonAdd,
                                            contentDescription = "Add Guest",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Ajouter comme invité",
                                                style = MaterialTheme.typography.body1,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = "Aucun joueur trouvé",
                                                style = MaterialTheme.typography.caption,
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is DataResult.Failure -> {
                        // Show guest option for failures too
                        if (shouldShowGuest && value.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                val guestId = -System.currentTimeMillis()
                                                onPlayerSelected("Invité", guestId)
                                                showDropdown = false
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PersonAdd,
                                            contentDescription = "Add Guest",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Ajouter comme invité",
                                                style = MaterialTheme.typography.body1,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = "Erreur de recherche",
                                                style = MaterialTheme.typography.caption,
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // Handle other states
                    }
                }
            }
        }

        // Selected player display (only show if showSelectedPlayerCard is true)
        if (showSelectedPlayerCard) {
            selectedPlayerId?.let { id ->
                val playerName = if (id < 0) {
                    "Invité" // Display "Invité" for guest players (negative IDs)
                } else {
                    findTermsViewModel.getPlayerById(id)?.fullName ?: ""
                }

                if (playerName.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(Color.LightGray.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (id < 0) Icons.Default.PersonAdd else Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (id < 0) Color.Gray.copy(alpha = 0.6f) else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = playerName,
                                    style = MaterialTheme.typography.body2,
                                    color = if (id < 0) Color.Gray.copy(alpha = 0.7f) else Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            IconButton(
                                onClick = {
                                    selectedPlayers.remove(id)
                                    onRemovePlayer()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Player",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdatedPartnerManagement(
    selectedParts: Int,
    findTermsViewModel: FindTermsViewModel,
    playerFullNames: List<String>
) {
    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())

    // Create individual states for each partner field
    val partner1 = remember { mutableStateOf(Pair("", null as Long?)) }
    val partner2 = remember { mutableStateOf(Pair("", null as Long?)) }
    val partner3 = remember { mutableStateOf(Pair("", null as Long?)) }

    // Clear all selections when selectedParts changes
    LaunchedEffect(selectedParts) {
        partner1.value = Pair("", null)
        partner2.value = Pair("", null)
        partner3.value = Pair("", null)
        selectedPlayers.clear()
        findTermsViewModel.resetGuestFallback()
    }

    Column {
        when (selectedParts) {
            1 -> {
                // Three partners needed
                PartnerField(
                    label = "Invité(e) 1",
                    value = partner1.value.first,
                    onValueChange = { text ->
                        partner1.value = Pair(text, partner1.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner1.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner1.value.second != null,
                    selectedPlayerId = null,
                    showSelectedPlayerCard = true, // Show card for multiple partners
                    onRemovePlayer = {
                        val playerId = partner1.value.second
                        partner1.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )

                PartnerField(
                    label = "Invité(e) 2",
                    value = partner2.value.first,
                    onValueChange = { text ->
                        partner2.value = Pair(text, partner2.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner2.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner2.value.second != null,
                    selectedPlayerId = null,
                    showSelectedPlayerCard = true, // Show card for multiple partners
                    onRemovePlayer = {
                        val playerId = partner2.value.second
                        partner2.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )

                PartnerField(
                    label = "Invité(e) 3",
                    value = partner3.value.first,
                    onValueChange = { text ->
                        partner3.value = Pair(text, partner3.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner3.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner3.value.second != null,
                    selectedPlayerId = null,
                    showSelectedPlayerCard = true, // Show card for multiple partners
                    onRemovePlayer = {
                        val playerId = partner3.value.second
                        partner3.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )
            }

            2 -> {
                // Two partners needed
                PartnerField(
                    label = "Invité(e) 1",
                    value = partner1.value.first,
                    onValueChange = { text ->
                        partner1.value = Pair(text, partner1.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner1.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner1.value.second != null,
                    selectedPlayerId = null,
                    showSelectedPlayerCard = true, // Show card for multiple partners
                    onRemovePlayer = {
                        val playerId = partner1.value.second
                        partner1.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )

                PartnerField(
                    label = "Invité(e) 2",
                    value = partner2.value.first,
                    onValueChange = { text ->
                        partner2.value = Pair(text, partner2.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner2.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner2.value.second != null,
                    selectedPlayerId = null,
                    showSelectedPlayerCard = true, // Show card for multiple partners
                    onRemovePlayer = {
                        val playerId = partner2.value.second
                        partner2.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )
            }

            3 -> {
                // One partner needed - special case
                PartnerField(
                    label = "Invité(e)",
                    value = partner1.value.first,
                    onValueChange = { text ->
                        partner1.value = Pair(text, partner1.value.second)
                    },
                    selectedPlayers = selectedPlayers,
                    findTermsViewModel = findTermsViewModel,
                    playerFullNames = playerFullNames,
                    onPlayerSelected = { selectedName, selectedId ->
                        partner1.value = Pair(selectedName, selectedId)
                        if (!selectedPlayers.contains(selectedId)) {
                            selectedPlayers.add(selectedId)
                        }
                    },
                    isDisabled = partner1.value.second != null,
                    selectedPlayerId = partner1.value.second,
                    showSelectedPlayerCard = false, // Hide card for single partner
                    onRemovePlayer = {
                        val playerId = partner1.value.second
                        partner1.value = Pair("", null)
                        playerId?.let { selectedPlayers.remove(it) }
                    }
                )
            }
        }

        // Display selected partners list for cases with 2 or 3 partners
        if (selectedParts == 1 || selectedParts == 2) {
            val selectedPartnerNames = mutableListOf<Pair<String, Long?>>()

            // Partner 1
            val partner1Name = partner1.value.second?.let { id ->
                if (id < 0) "Invité" else findTermsViewModel.getPlayerById(id)?.fullName ?: partner1.value.first
            } ?: if (partner1.value.first.isEmpty()) "Invité" else partner1.value.first
            selectedPartnerNames.add(Pair(partner1Name, partner1.value.second))

            if (selectedParts == 1) {
                // Partner 2
                val partner2Name = partner2.value.second?.let { id ->
                    if (id < 0) "Invité" else findTermsViewModel.getPlayerById(id)?.fullName ?: partner2.value.first
                } ?: if (partner2.value.first.isEmpty()) "Invité" else partner2.value.first
                selectedPartnerNames.add(Pair(partner2Name, partner2.value.second))

                // Partner 3
                val partner3Name = partner3.value.second?.let { id ->
                    if (id < 0) "Invité" else findTermsViewModel.getPlayerById(id)?.fullName ?: partner3.value.first
                } ?: if (partner3.value.first.isEmpty()) "Invité" else partner3.value.first
                selectedPartnerNames.add(Pair(partner3Name, partner3.value.second))
            } else if (selectedParts == 2) {
                // Partner 2
                val partner2Name = partner2.value.second?.let { id ->
                    if (id < 0) "Invité" else findTermsViewModel.getPlayerById(id)?.fullName ?: partner2.value.first
                } ?: if (partner2.value.first.isEmpty()) "Invité" else partner2.value.first
                selectedPartnerNames.add(Pair(partner2Name, partner2.value.second))
            }
        }
    }
}

@Composable
private fun DropdownPlayerItem(
    playerName: String,
    onPlayerClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp),
        color = Color.Transparent
    ) {
        Text(
            text = playerName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            style = MaterialTheme.typography.body1
        )
    }
}

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-8).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "  Je commande des extras ?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = additionalExtrasEnabled,
            onCheckedChange = { isEnabled ->
                additionalExtrasEnabled = isEnabled

                // Clear all selected extras when switch is turned off
                if (!isEnabled) {
                    // Clear the lists
                    sharedList.value.clear()
                    privateList.value.clear()

                    // Update ViewModels with empty lists
                    findTermsViewModel.updateSharedExtras(sharedList.value)
                    findTermsViewModel.updatePrivateExtras(privateList.value)

                    // Clear selected extras
                    selectedExtras = emptyList()

                    // Update parent with empty list and zero cost
                    onExtrasUpdate(emptyList(), 0.0)
                }
            },
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

                Spacer(modifier = Modifier.height(1.dp))

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = "  Article(s) réserver à mon usage",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    privateExtrasList?.forEach { privateExtra ->
                        val isPrivateExtraAdded =
                            selectedExtras.any { it.first == privateExtra.name }
                        ExtraItemCard(
                            extra = privateExtra,
                            isAdded = isPrivateExtraAdded,
                            onAddClick = { extraPrice ->
                                privateList.value.add(privateExtra.id)
                                findTermsViewModel.updatePrivateExtras(privateList.value)

                                selectedExtras += Triple(
                                    privateExtra.name,
                                    privateExtra.amount.toString(),
                                    privateExtra.currencyId.toInt()
                                )
                                val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() } * 4
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                                Log.d("ahmed","$totalExtrasCost")
                                Log.d("money","$totalExtrasCost")

                            },
                            onRemoveClick = { extraPrice ->
                                privateList.value.remove(privateExtra.id)
                                findTermsViewModel.updatePrivateExtras(privateList.value)
                                selectedExtras =
                                    selectedExtras.filterNot { it.first == privateExtra.name }
                                val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() } * 4
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Column {
                        Text(
                            text = "  Article(s) partagé(s) entre tous les joueurs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            // color = Color.Black
                        )
                        Text(
                            text = "  (Frais partagé)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
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
                                findTermsViewModel.updateSharedExtras(sharedList.value)

                                val sharedAmount = when (selectedParts.toInt()) {
                                    1 -> (extraPrice / 4.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                                    2 -> (extraPrice / 2.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                                    3 -> (extraPrice /4 * 3).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                                    4 -> (extraPrice / 1.0).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                                    else -> extraPrice.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                                }
                                val sharedamountt = sharedAmount * 4
                                selectedExtras += Triple(
                                    sharedExtra.name,
                                    sharedamountt.toString(),
                                    sharedExtra.currencyId.toInt()
                                )
                                val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() }
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            },
                            onRemoveClick = { extraPrice ->
                                sharedList.value.remove(sharedExtra.id)
                                findTermsViewModel.updateSharedExtras(sharedList.value)
                                selectedExtras =
                                    selectedExtras.filterNot { it.first == sharedExtra.name }
                                val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() }
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
                .size(30.dp)
                .padding(start = 4.dp, top = 4.dp)
                .offset(y = 4.dp)
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
            val extraPrice = pricePerPart

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







