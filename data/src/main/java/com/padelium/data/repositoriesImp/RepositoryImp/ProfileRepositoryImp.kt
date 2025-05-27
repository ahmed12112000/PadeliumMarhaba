package com.padelium.data.repositoriesImp.RepositoryImp

import android.content.Context
import android.net.Uri
import com.padelium.domain.repositories.IProfileRepository
import com.padelium.data.datasource.remote.PadeliumApi
import javax.inject.Inject
import retrofit2.Response


class ProfileRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IProfileRepository {

    override suspend fun Profile(context: Context, accountJson: String, imageUri: Uri?): Response<Void> {
        return api.Profile(accountJson, imageUri)
    }
}




