package com.padelium.data.mappers


import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.SignupRequestDTO
import com.padelium.data.dto.logoutRequestDTO
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun signupRequestToSignupRequestDto(signupRequest: SignupRequest): SignupRequestDTO {
        return SignupRequestDTO(
            firstName = signupRequest.firstName,
            lastName = signupRequest.lastName,
            password = signupRequest.password,
            email = signupRequest.email
        )
    }

    fun logoutRequestTologoutRequestDto(logoutRequest: logoutRequest): logoutRequestDTO {
        return logoutRequestDTO()  // Empty DTO as no actual data is needed in the request
    }

}