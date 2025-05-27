package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetProfileByIdRepository
import com.padelium.domain.repositories.IGetReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetProfileByIdViewModel @Inject constructor(
    private val reservationRepository: IGetReservationRepository,
    private val profileRepository: IGetProfileByIdRepository
) : ViewModel() {

    private val _reservationsData =
        MutableLiveData<DataResultBooking<GetReservationResponse>>()
    val reservationsData: LiveData<DataResultBooking<GetReservationResponse>> get() = _reservationsData

    private val _profilesData =
        MutableLiveData<DataResultBooking<GetReservationIDResponse>>()
    val profilesData: LiveData<DataResultBooking<GetReservationIDResponse>> get() = _profilesData


    /**
     * Fetch profile by reservation ID.
     */
    fun fetchProfileById(reservationId: Long) {
        _profilesData.postValue(DataResultBooking.Loading)

        viewModelScope.launch {
            try {
                val response = profileRepository.GetProfileById(reservationId)

                if (response != null) {
                    _profilesData.postValue(DataResultBooking.Success(response))
                } else {
                    _profilesData.postValue(
                        DataResultBooking.Failure(
                            exception = null,
                            errorCode = null,
                            errorMessage = "No profile found for ID: $reservationId."
                        )
                    )
                }
            } catch (e: Exception) {
                _profilesData.postValue(
                    DataResultBooking.Failure(
                        exception = e,
                        errorCode = null,
                        errorMessage = e.localizedMessage ?: "Unknown error occurred"
                    )
                )
            }
        }
    }
}


