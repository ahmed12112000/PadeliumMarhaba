package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.usecases.SaveBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveBookingViewModel @Inject constructor(
    private val saveBookingUseCase: SaveBookingUseCase
) : ViewModel() {


    private val _dataResult = MutableLiveData<DataResult>()
    val dataResult: LiveData<DataResult> = _dataResult

    private var bookingIds: Long? = null
    private val _bookingId = MutableLiveData<Long?>()
    val bookingId: LiveData<Long?> = _bookingId

    fun SaveBooking(getBookingResponses: List<GetBookingResponse>) {
        viewModelScope.launch {
            _dataResult.value = DataResult.Loading

            try {
                val result = saveBookingUseCase.SaveBooking(getBookingResponses)
                _dataResult.value = result

                if (result is DataResult.Success) {
                    val saveBookingResponse = result.data as? SaveBookingResponse
                    saveBookingResponse?.let {
                        bookingIds = it.id
                        _bookingId.value = bookingIds
                    }

                }
            } catch (e: Exception) {
                _dataResult.value = DataResult.Failure(exception = e, errorCode = null, errorMessage = e.message ?: "Unknown error")
            }
        }
    }
}













