package com.padelium.domain.usecases

import android.content.Context
import android.net.Uri
import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) {
    suspend fun Profile(accountJson: String, imageUri: Uri?, context: Context): DataResult {
        return try {
            val response = profileRepository.Profile(context, accountJson, imageUri)

            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }

        } catch (ex: Exception) {
            DataResult.Failure(ex, null, "Error during profile update: ${ex.localizedMessage}")
        }
    }
}










