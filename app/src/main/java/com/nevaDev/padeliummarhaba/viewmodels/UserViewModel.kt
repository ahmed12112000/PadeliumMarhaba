package com.nevaDev.padeliummarhaba.viewmodels

import android.content.SharedPreferences
import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import com.padelium.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    private val _dataResult1 = MutableLiveData<Resulta>()
    val dataResult1: LiveData<Resulta> get() = _dataResult1

    private val _loginResult = MutableLiveData<Resulta?>()
    val loginResult: MutableLiveData<Resulta?> get() = _loginResult
    /**
     * Start getting data
     */
    suspend fun loginUser(loginRequest: LoginRequest): Resulta {
        return try {
            _dataResult1.postValue(Resulta.Loading)
            val result = userUseCase.loginUser(loginRequest)
            _dataResult1.postValue(result)
            _loginResult.postValue(result)
            result
        } catch (e: Exception) {
            val failureResult = Resulta.Failure(e, null, 500, e.message ?: "Unknown error")
            _dataResult1.postValue(failureResult)
            _loginResult.postValue(failureResult)
            failureResult
        }
    }

    /**
     * Start signup
     */
    fun signupUser(signupRequest: SignupRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value = userUseCase.signupUser(signupRequest)
        }
    }

}

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData

    private val _logoutState = MutableStateFlow<DataResult>(DataResult.Loading)
    val logoutState: StateFlow<DataResult> get() = _logoutState

    fun logoutUser(logoutRequest: logoutRequest) {
        viewModelScope.launch {
            _logoutState.value = DataResult.Loading
            try {
                val result = userUseCase.logoutUser(logoutRequest)

                if (result is DataResult.Success) {
                    sharedPreferences.edit().clear().apply()

                    _userData.value = null

                    _logoutState.value = DataResult.Success(Unit)
                } else {
                    _logoutState.value = result
                }
            } catch (e: Exception) {
                _logoutState.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "Logout failed"
                )
            }
        }
    }
}