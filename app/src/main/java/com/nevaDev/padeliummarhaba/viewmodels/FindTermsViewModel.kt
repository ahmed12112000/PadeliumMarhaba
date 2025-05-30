package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.FindTermsResponse
import com.padelium.domain.usecases.FindTermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject
@HiltViewModel
class FindTermsViewModel @Inject constructor(
    private val findTermsUseCase: FindTermsUseCase
) : ViewModel() {
    private val _players = MutableLiveData<DataResult>()
    val players: LiveData<DataResult> = _players

    private val _playerFullNames = MutableLiveData<List<String>>(emptyList())
    val playerFullNames: LiveData<List<String>> = _playerFullNames

    private val _selectedPlayers = MutableLiveData<MutableList<Long>>(mutableListOf())
    val selectedPlayers: LiveData<MutableList<Long>> = _selectedPlayers

    private var allPlayersData: List<FindTermsResponse> = emptyList()
    private var searchJob: Job? = null
    private var guestFallbackJob: Job? = null

    private val _sharedExtras = MutableLiveData<List<Long>>(emptyList())
    val sharedExtras: LiveData<List<Long>> = _sharedExtras

    private val _privateExtras = MutableLiveData<List<Long>>(emptyList())
    val privateExtras: LiveData<List<Long>> = _privateExtras

    val navigationEvent = MutableLiveData<String>()

    // New property to track if we should show guest option
    private val _shouldShowGuest = MutableLiveData<Boolean>(false)
    val shouldShowGuest: LiveData<Boolean> = _shouldShowGuest

    fun updateSharedExtras(newSharedExtras: List<Long>) {
        _sharedExtras.value = newSharedExtras
    }

    fun updatePrivateExtras(newPrivateExtras: List<Long>) {
        _privateExtras.value = newPrivateExtras
    }

    /**
     * Fetch players matching the search term with an optional limit to restrict the number of players fetched.
     */
    fun findTerms(term: RequestBody, limit: Int? = null) {
        searchJob?.cancel()
        guestFallbackJob?.cancel()
        _shouldShowGuest.value = false

        searchJob = viewModelScope.launch {
            delay(150)
            _players.value = DataResult.Loading
            try {
                val result = findTermsUseCase.FindTerms(term)
                if (result is DataResult.Success) {
                    allPlayersData = result.data as? List<FindTermsResponse> ?: emptyList()
                    val limitedResults = limit?.let {
                        allPlayersData.take(it)
                    } ?: allPlayersData
                    _players.value = DataResult.Success(limitedResults)
                    _playerFullNames.value = limitedResults.map { it.fullName }

                    // If no players found, start the guest fallback timer
                    if (limitedResults.isEmpty()) {
                        startGuestFallbackTimer()
                    }
                } else {
                    _players.value = result
                    if (result is DataResult.Failure && result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                    // Also start guest fallback for failures
                    startGuestFallbackTimer()
                }
            } catch (e: Exception) {
                _players.value = DataResult.Failure(exception = e, errorCode = null, errorMessage = e.localizedMessage)
                // Start guest fallback for exceptions
                startGuestFallbackTimer()
            }
        }
    }

    /**
     * Start a timer to show guest option after 3 seconds if no players are found
     */
    private fun startGuestFallbackTimer() {
        guestFallbackJob = viewModelScope.launch {
            delay(2000) // 3 seconds
            _shouldShowGuest.value = true
        }
    }

    /**
     * Reset the guest fallback state
     */
    fun resetGuestFallback() {
        guestFallbackJob?.cancel()
        _shouldShowGuest.value = false
    }

    /**
     * Get a player by their full name.
     */
    fun getPlayerByFullName(fullName: String): FindTermsResponse? {
        return allPlayersData.find { it.fullName.equals(fullName, ignoreCase = true) }
    }

    fun getPlayerById(id: Long): FindTermsResponse? {
        return allPlayersData.find { it.id == id }
    }
}





