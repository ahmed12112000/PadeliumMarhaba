package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetStatusesResponse
import com.padelium.domain.repositories.IGetStatusesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetStatusesViewModel @Inject constructor(private val repository: IGetStatusesRepository) : ViewModel()
{

    private val _ReservationsData1 =
        MutableLiveData<DataResultBooking<List<GetStatusesResponse>>>()
    val ReservationsData1: LiveData<DataResultBooking<List<GetStatusesResponse>>> get() = _ReservationsData1

    fun GetStatuses() {
        _ReservationsData1.postValue(DataResultBooking.Loading)

        viewModelScope.launch {
            try {
                val response = repository.GetStatuses()

                if (response.isNotEmpty()) {
                    _ReservationsData1.postValue(DataResultBooking.Success(response))
                } else {
                    _ReservationsData1.postValue(
                        DataResultBooking.Failure(
                            exception = null,
                            errorCode = null,
                            errorMessage = "No reservations found."
                        )
                    )
                }
            } catch (e: Exception) {
                _ReservationsData1.postValue(
                    DataResultBooking.Failure(
                        exception = e,
                        errorCode = null,
                        errorMessage = "Exception occurred: ${e.message}"
                    )
                )
            }
        }
    }
}
