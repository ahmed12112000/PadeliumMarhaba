package com.padelium.domain.repositories

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface IProfileRepository {

    suspend fun Profile(context: Context, accountJson: String, imageUri: Uri?): Response<Void>
}

