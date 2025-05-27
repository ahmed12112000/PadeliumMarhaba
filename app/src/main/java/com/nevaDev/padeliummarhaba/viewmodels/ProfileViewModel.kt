package com.nevaDev.padeliummarhaba.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val application: Application
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start getting data for profile update
     * @param accountJson A JSON string containing account details (e.g., "nom", "prenom").
     * @param imageUri The URI of the image being uploaded.
     */
    fun Profile(accountJson: String, imageUri: Uri?) {
        dataResult.value = DataResult.Loading

        viewModelScope.launch {
            val result = profileUseCase.Profile(accountJson, imageUri, application.applicationContext)

            dataResult.value = result
        }
    }
}





