package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.repositories.IInitBookingRepository
import javax.inject.Inject
import android.util.Log
import com.google.gson.JsonSyntaxException
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import retrofit2.HttpException

class InitBookingUseCase @Inject constructor(
    private val initBookingRepository: IInitBookingRepository
) {

    suspend fun InitBooking(initBookingRequest: InitBookingRequest): DataResultBooking<List<InitBookingResponse>> {
        return try {
            Log.d("InitBookingUseCase", "=== STARTING InitBooking ===")
            Log.d("InitBookingUseCase", "Request: $initBookingRequest")

            val response = initBookingRepository.InitBooking(initBookingRequest)

            Log.d("InitBookingUseCase", "=== RESPONSE RECEIVED ===")
            Log.d("InitBookingUseCase", "Response code: ${response.code()}")
            Log.d("InitBookingUseCase", "Response message: ${response.message()}")
            Log.d("InitBookingUseCase", "Is successful: ${response.isSuccessful}")
            Log.d("InitBookingUseCase", "Headers: ${response.headers()}")

            if (response.isSuccessful) {
                Log.d("InitBookingUseCase", "=== PROCESSING SUCCESSFUL RESPONSE ===")
                val body = response.body()

                Log.d("InitBookingUseCase", "Body is null: ${body == null}")

                if (body != null) {
                    Log.d("InitBookingUseCase", "Body type: ${body::class.java.simpleName}")
                    Log.d("InitBookingUseCase", "Body size: ${body.size}")

                    // Log first item details if available
                    if (body.isNotEmpty()) {
                        val firstItem = body[0]
                        Log.d("InitBookingUseCase", "First item: establishmentDTO.id = ${firstItem.establishmentDTO?.id}")
                        Log.d("InitBookingUseCase", "First item: plannings size = ${firstItem.plannings?.size}")
                    }

                    Log.d("InitBookingUseCase", "=== RETURNING SUCCESS ===")
                    DataResultBooking.Success(body)
                } else {
                    Log.e("InitBookingUseCase", "=== BODY IS NULL ===")
                    // Try to get raw response
                    val rawResponse = response.raw().toString()
                    Log.e("InitBookingUseCase", "Raw response: $rawResponse")
                    DataResultBooking.Failure(null, response.code(), "Response body is null")
                }
            } else {
                Log.e("InitBookingUseCase", "=== HTTP ERROR ===")
                val errorBody = response.errorBody()?.string()
                Log.e("InitBookingUseCase", "Error body: $errorBody")
                DataResultBooking.Failure(null, response.code(), errorBody ?: "HTTP Error ${response.code()}")
            }
        } catch (ex: HttpException) {
            Log.e("InitBookingUseCase", "=== HTTP EXCEPTION ===", ex)
            Log.e("InitBookingUseCase", "HttpException code: ${ex.code()}")
            Log.e("InitBookingUseCase", "HttpException message: ${ex.message()}")
            DataResultBooking.Failure(ex, ex.code(), ex.localizedMessage ?: "HTTP Exception")
        } catch (ex: JsonSyntaxException) {
            Log.e("InitBookingUseCase", "=== JSON PARSING EXCEPTION ===", ex)
            Log.e("InitBookingUseCase", "JSON parsing error: ${ex.message}")
            DataResultBooking.Failure(ex, 500, "JSON parsing error: ${ex.localizedMessage}")
        } catch (ex: Exception) {
            Log.e("InitBookingUseCase", "=== GENERIC EXCEPTION ===", ex)
            Log.e("InitBookingUseCase", "Exception type: ${ex::class.java.simpleName}")
            Log.e("InitBookingUseCase", "Exception message: ${ex.message}")
            Log.e("InitBookingUseCase", "Exception cause: ${ex.cause}")
            ex.printStackTrace()
            DataResultBooking.Failure(ex, 500, ex.localizedMessage ?: "Unknown error")
        }
    }
}






