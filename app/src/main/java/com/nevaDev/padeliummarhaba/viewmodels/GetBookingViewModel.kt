package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.mappers.GetBookingMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.GetBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class GetBookingViewModel @Inject constructor(
    private val getBookingUseCase: GetBookingUseCase,
    private val getBookingMapper: GetBookingMapper
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<List<GetBookingResponseDTO>>>()

    private val _timeSlots = MutableLiveData<List<TimeSlot>>()
    private val _filteredTimeSlots = MutableLiveData<List<TimeSlot>>()
    val filteredTimeSlots: LiveData<List<TimeSlot>> get() = _filteredTimeSlots

    private val _parsedTimeSlotss = MutableStateFlow<List<TimeSlot>>(emptyList())
    val parsedTimeSlots: StateFlow<List<TimeSlot>> get() = _parsedTimeSlotss

    private val _selectedBookings = MutableLiveData<List<GetBookingResponseDTO>>(emptyList())

    val navigationEvent = MutableLiveData<String>()

    fun updateBookings(newBookings: List<GetBookingResponseDTO>) {
        _selectedBookings.value = newBookings
    }

    fun getBooking(key: String, selectedDate: LocalDate) {
        dataResultBooking.value = DataResultBooking.Loading
        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                dataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val mappedData = getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)

                        val parsedTimeSlots = mappedData.flatMap { booking ->
                            booking.plannings.mapNotNull { planning ->
                                val timeSlot = parseTimeSlot(planning.fromStr, selectedDate)
                                if (timeSlot != null && isAvailable(timeSlot)) {
                                    timeSlot
                                } else {
                                    null
                                }
                            }
                        }

                        _timeSlots.postValue(parsedTimeSlots)

                        if (parsedTimeSlots.isNotEmpty()) {
                            filterSlotsByDate(selectedDate, parsedTimeSlots)
                            _parsedTimeSlotss.value = parsedTimeSlots
                        } else {
                            _filteredTimeSlots.postValue(emptyList())
                        }

                        DataResultBooking.Success(mappedData)
                    }

                    is DataResultBooking.Failure -> {
                        if (result.errorCode != 200) {
                            // Trigger the navigation event if errorCode is not 200
                            navigationEvent.value = "server_error_screen"
                        }
                        DataResultBooking.Failure(exception = null, errorCode = result.errorCode,errorMessage = ""
                        )
                    }

                    else -> {
                        DataResultBooking.Failure(exception = null, errorCode = null, errorMessage = "")
                    }
                }
            } catch (e: Exception) {
                dataResultBooking.value = DataResultBooking.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = ""
                )
            }
        }
    }
    private fun isAvailable(timeSlot: TimeSlot): Boolean {
        val bookedSlots = _selectedBookings.value?.flatMap { booking ->
            booking.plannings.mapNotNull { planning ->
                parseTimeSlot(planning.fromStr, timeSlot.date)
            }
        } ?: emptyList()

        return bookedSlots.none { it.date == timeSlot.date && it.time == timeSlot.time }
    }
    private fun parseTimeSlot(fromStr: String, date: LocalDate): TimeSlot? {
        return try {
            val localTime =
                LocalTime.parse(fromStr, DateTimeFormatter.ofPattern("H:mm"))
            TimeSlot(date = date, time = localTime)
        } catch (e: Exception) {
            null
        }
    }

    fun filterSlotsByDate(selectedDate: LocalDate, parsedTimeSlots: List<TimeSlot>) {
        viewModelScope.launch {
            try {
                val currentDateTime = ZonedDateTime.now(ZoneId.of("Africa/Tunis"))
                val isToday = selectedDate.isEqual(currentDateTime.toLocalDate())

                if (parsedTimeSlots.isEmpty()) {
                    _filteredTimeSlots.postValue(emptyList())
                    return@launch
                }

                val filteredSlots = parsedTimeSlots.filter { slot ->
                    val slotDateTime = ZonedDateTime.of(slot.date.atTime(slot.time), ZoneId.of("Africa/Tunis"))

                    if (isToday) {
                        slotDateTime.isAfter(currentDateTime)
                    } else {
                        slot.date.isEqual(selectedDate)
                    }
                }
                _filteredTimeSlots.postValue(filteredSlots)

            } catch (e: Exception) {
                _filteredTimeSlots.postValue(emptyList())
            }
        }
    }

}

data class TimeSlot(
    val date: LocalDate,
    val time: LocalTime
)
