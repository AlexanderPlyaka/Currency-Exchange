package com.obriylabs.currencyandroid.interactor

import com.obriylabs.currencyandroid.repository.ExchangersRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class Exchangers
@Inject constructor(private val exchangersRepository: ExchangersRepository) : UseCase<ResponseBody, String> {

    override suspend fun run(params: String) = exchangersRepository.getExchangers(params)

}