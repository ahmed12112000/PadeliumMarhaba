package com.padelium.domain.usecases

import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import com.padelium.domain.repositories.IUserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserUseCase @Inject constructor(private val userRepository: IUserRepository) {
    suspend fun loginUser(loginRequest: LoginRequest): Resulta {
        return try {
            val response = userRepository.loginUser(loginRequest)

            if (response.isSuccessful) {
                Resulta.Success(response.body() ?: "No data available")
            } else {
                val errorMessage = response.errorBody()?.string()?.takeIf { it.isNotEmpty() } ?: "Unknown error occurred"

                if (response.code() == 401) { // Unauthorized (Wrong credentials)
                    Resulta.Failure(null, response.code(), response.code(), "Invalid credentials. Please try again.")
                } else {
                    Resulta.Failure(null, response.code(), response.code(), errorMessage)
                }
            }
        } catch (ex: HttpException) {
            Resulta.Failure(ex, null, ex.code(), ex.localizedMessage ?: "An error occurred")
        } catch (ex: Exception) {
            Resulta.Failure(ex, null, 500, ex.localizedMessage ?: "An error occurred")
        }
    }
    suspend fun signupUser(signupRequest: SignupRequest): DataResult {
        return try {
            val response = userRepository.signupUser(signupRequest)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during signup")
        }
    }
    suspend fun logoutUser(logoutRequest: logoutRequest): DataResult {
        return try {
            val response = userRepository.logoutUser(logoutRequest)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during signup")
        }
    }
}