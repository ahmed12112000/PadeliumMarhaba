package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.repositories.ISaveBookingRepository
import retrofit2.Response
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class SaveBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : ISaveBookingRepository {

    override suspend fun SaveBooking(saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>> {
        return api.SaveBooking(saveBookingRequest)
    }
}

