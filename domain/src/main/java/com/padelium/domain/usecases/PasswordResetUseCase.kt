package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IDeleteAccountRepository
import com.padelium.domain.repositories.IGetPasswordRepository
import com.padelium.domain.repositories.IResetPasswordRepository
import javax.inject.Inject


class GetPasswordUseCase @Inject constructor(
    private val getPasswordRepository: IGetPasswordRepository
) {
    suspend fun GetPassword(email: String): DataResult {
        return try {
            val response = getPasswordRepository.GetPassword(email)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment processing")
        }
    }
}


class ResetPasswordUseCase @Inject constructor(
    private val resetPasswordRepository: IResetPasswordRepository
) {
    suspend fun ResetPassword(email: String): DataResult {
        return try {
            val response = resetPasswordRepository.ResetPassword(email)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment processing")
        }
    }
}

class DeleteAccountUseCase @Inject constructor(
    private val deleteAccountRepository: IDeleteAccountRepository
) {
    suspend fun DeleteAccount(email: String): DataResult {
        return try {
            val response = deleteAccountRepository.DeleteAccount(email)
            if (response.isSuccessful) {
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment processing")
        }
    }
}


