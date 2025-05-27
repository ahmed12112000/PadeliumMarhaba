package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.repositories.IGetProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetProfileViewModel @Inject constructor(private val repository: IGetProfileRepository) : ViewModel() {

    private val _profileData = MutableLiveData<DataResultBooking<GetProfileResponse>>()
    val profileData: LiveData<DataResultBooking<GetProfileResponse>> get() = _profileData

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> get() = _lastName

    private val _image = MutableLiveData<String?>()
    val image: LiveData<String?> get() = _image

    private val _hasUserRole = MutableLiveData<Boolean>()
    val hasUserRole: LiveData<Boolean> get() = _hasUserRole

    private val _activated = MutableLiveData<Boolean>()
    val activated: LiveData<Boolean> get() = _activated

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _authorities = MutableLiveData<List<String>>()
    val authorities: LiveData<List<String>> get() = _authorities

    private val _login = MutableLiveData<String>()
    val login: LiveData<String> get() = _login

    private val _langkey = MutableLiveData<String>()
    val langkey: LiveData<String> get() = _langkey

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> get() = _phone

    fun fetchProfileData() {
        _profileData.postValue(DataResultBooking.Loading)

        viewModelScope.launch {
            try {
                // Add logging to see what's happening
                Log.d("ProfileViewModel", "Starting profile fetch...")

                val profile = repository.GetProfile()

                // Log the received profile data
                Log.d("ProfileViewModel", "Profile received: $profile")
                Log.d("ProfileViewModel", "FirstName: ${profile.firstName}")
                Log.d("ProfileViewModel", "LastName: ${profile.lastName}")
                Log.d("ProfileViewModel", "Email: ${profile.email}")
                Log.d("ProfileViewModel", "Phone: ${profile.phone}")

                // Post success first
                _profileData.postValue(DataResultBooking.Success(profile))

                // Then update individual fields with null safety
                _firstName.postValue(profile.firstName ?: "")
                _lastName.postValue(profile.lastName ?: "")
                _image.postValue(profile.image)
                _email.postValue(profile.email ?: "")
                _login.postValue(profile.login ?: "")
                _langkey.postValue(profile.langKey ?: "")
                _phone.postValue(profile.phone ?: "")
                _activated.postValue(profile.activated ?: false)
                _authorities.postValue(profile.authorities ?: emptyList())
                _hasUserRole.postValue((profile.authorities ?: emptyList()).contains("ROLE_USER"))

                Log.d("ProfileViewModel", "Profile data updated successfully")

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile: ${e.message}", e)
                Log.e("ProfileViewModel", "Exception type: ${e.javaClass.simpleName}")
                Log.e("ProfileViewModel", "Stack trace: ${e.stackTrace.joinToString("\n")}")

                _profileData.postValue(
                    DataResultBooking.Failure(e, null, "Exception occurred: ${e.message}")
                )
            }
        }
    }
}

