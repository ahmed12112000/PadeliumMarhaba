package com.padelium.data.repositoriesImp.RepositoryImp
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IUpdatePhoneRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject



class UpdatePhoneRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IUpdatePhoneRepository {

    override suspend fun UpdatePhone(Phone: RequestBody): Response<Unit> {
        return api.UpdatePhone(Phone)
    }
}
