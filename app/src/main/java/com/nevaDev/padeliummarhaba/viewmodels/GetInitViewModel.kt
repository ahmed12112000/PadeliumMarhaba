package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.GetInitResponseDTO
import com.padelium.data.mappers.GetInitMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.GetInitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GetInitViewModel @Inject constructor(
    private val getInitUseCase: GetInitUseCase,
    private val getInitMapper: GetInitMapper,
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<GetInitResponseDTO>>()

    val navigationEvent = MutableLiveData<String>()

    fun GetInit(key: String) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = getInitUseCase.execute(key)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val getInitResponseDTO = getInitMapper.GetInitResponseToGetInitResponseDto(result.data)

                    DataResultBooking.Success(getInitResponseDTO)
                }
                is DataResultBooking.Failure -> {
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                    DataResultBooking.Failure(
                        exception = null,
                        errorCode = result.errorCode,
                        errorMessage = result.errorMessage
                    )
                }
                else -> {
                    DataResultBooking.Failure(

                        exception = null,
                        errorCode = null,
                        errorMessage = "Unknown error"
                    )
                }
            }
        }
    }
}
