package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.DeleteAccountUseCase
import com.padelium.domain.usecases.GetPasswordUseCase
import com.padelium.domain.usecases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPasswordViewModel @Inject constructor(
    private val getPasswordUseCase: GetPasswordUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start processing payment
     */
    fun GetPassword(email: String) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = getPasswordUseCase.GetPassword(email)
            dataResult.value = result
            when (result) {
                is DataResult.Success ->{}
                is DataResult.Failure -> {}
                else -> {}
            }
        }
    }
}


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start processing payment
     */
    fun ResetPassword(email: String) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = resetPasswordUseCase.ResetPassword(email)
            dataResult.value = result
            when (result) {
                is DataResult.Success ->{}
                is DataResult.Failure -> {}
                else -> {}
            }
        }
    }
}

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    /**
     * Start processing payment
     */
    fun DeleteAccount(email: String) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = deleteAccountUseCase.DeleteAccount(email)
            dataResult.value = result
            when (result) {
                is DataResult.Success ->{}
                is DataResult.Failure -> {}
                else -> {}
            }
        }
    }
}
