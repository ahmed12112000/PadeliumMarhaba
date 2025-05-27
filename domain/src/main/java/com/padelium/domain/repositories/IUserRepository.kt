package com.padelium.domain.repositories


import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import okhttp3.ResponseBody
import retrofit2.Response


interface IUserRepository {
    suspend fun signupUser(loginRequest: SignupRequest): Response<Void>

    suspend fun loginUser(loginRequest: LoginRequest): Response<ResponseBody>
    suspend fun logoutUser(loginRequest: logoutRequest): Response<ResponseBody>

}