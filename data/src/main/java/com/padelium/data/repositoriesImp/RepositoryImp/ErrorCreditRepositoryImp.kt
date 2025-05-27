package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.ErrorCreditMapper
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.repositories.IErrorCreditRepository
import retrofit2.Response
import javax.inject.Inject


class ErrorCreditRepositoryImp @Inject constructor(
    private val apiService: PadeliumApi,
    private val mapper: ErrorCreditMapper
) : IErrorCreditRepository {




    override suspend fun ErrorCredit(creditErrorRequest: CreditErrorRequest): Response<Void> {
        return apiService.ErrorCredit(mapper.CreditErrorRequestToCreditErrorRequestDto(creditErrorRequest))
    } }

