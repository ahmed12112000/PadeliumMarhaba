package com.padelium.data.repositoriesImp.RepositoryImp


import com.padelium.domain.dto.LoginRequest
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.UserMapper
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import com.padelium.domain.repositories.IUserRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
class UserRepositoryImp @Inject constructor(
    private val apiService: PadeliumApi,
    private val mapper: UserMapper
) : IUserRepository {

    override suspend fun loginUser(loginRequest: LoginRequest): Response<ResponseBody> {
        return apiService.loginUser(loginRequest.username, loginRequest.password)
    }

    override suspend fun signupUser(signupRequest: SignupRequest): Response<Void> {
        return apiService.signup(mapper.signupRequestToSignupRequestDto(signupRequest))
    }

    override suspend fun logoutUser(logoutRequestt: logoutRequest): Response<ResponseBody> {
        return apiService.logoutUser(mapper.logoutRequestTologoutRequestDto(logoutRequestt))
    }

}
