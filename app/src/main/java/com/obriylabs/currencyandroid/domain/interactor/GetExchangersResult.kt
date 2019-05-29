package com.obriylabs.currencyandroid.domain.interactor

import com.obriylabs.currencyandroid.data.model.ExchangersResult
import com.obriylabs.currencyandroid.data.repository.IExchangersRepository
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure
import javax.inject.Inject

class GetExchangersResult
@Inject constructor(private val exchangersRepository: IExchangersRepository) : UseCase<ExchangersResult, GetExchangersResult.Params> {

    override suspend fun run(params: Params) : Result<Failure, ExchangersResult> {
        return exchangersRepository.exchangersFileHandler(params.receivedExchangers.responseBody.bytes())
    }

    data class Params(val receivedExchangers: ReceivedExchangers)

}