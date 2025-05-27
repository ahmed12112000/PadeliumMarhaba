package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking

import com.padelium.domain.dto.SearchListResponse
import com.padelium.domain.repositories.ISearchListRepository
import javax.inject.Inject


class SearchListUseCase @Inject constructor(private val searchListRepository: ISearchListRepository) {

    suspend fun  execute(key: String): DataResultBooking<List<SearchListResponse>> {
        return try {
            val response = searchListRepository.searchlist(key)

            if (response.isSuccessful) {
                val searchListResponse = response.body()
                if (searchListResponse != null) {
                    DataResultBooking.Success(searchListResponse)
                } else {
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (e: Exception) {
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the GetInit request")
        }
    }
}
