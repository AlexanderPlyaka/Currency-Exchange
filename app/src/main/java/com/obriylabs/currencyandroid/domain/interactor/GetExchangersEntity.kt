package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetExchangersEntity
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<ExchangersEntity, GetExchangersEntity.Params> {

    override suspend fun run(params: Params) : Result<Failure, ExchangersEntity> {
        return exchangersRepository.exchangersFileHandler(params.receivedExchangers.responseBody.bytes())
    }

    data class Params(val receivedExchangers: ReceivedExchangers)

}