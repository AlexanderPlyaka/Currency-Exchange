package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.repository.INetworkRepository
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetExchangersEntity
@Inject constructor(private val networkRepository: INetworkRepository) : UseCase<ExchangersEntity, GetExchangersEntity.Params> {

    override suspend fun run(params: Params) : Result<Failure, ExchangersEntity> {
        return networkRepository.fileHandler(params.inputData.responseBody.bytes())
    }

    data class Params(val inputData: InputData)

}