package com.obriylabs.currencyandroid.interactor

import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.interactor.UseCase
import com.obriylabs.currencyandroid.utils.DataHandler
import javax.inject.Inject

class ExchangersFile
@Inject constructor(private val dataHandler: DataHandler) : UseCase<ExchangersResponse, ByteArray> {

    override suspend fun run(params: ByteArray) = dataHandler.getExchangers(params)

}
