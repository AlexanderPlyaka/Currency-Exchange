package com.obriylabs.currencyandroid.repository

import com.obriylabs.currencyandroid.api.DateResponse
import com.obriylabs.currencyandroid.api.ExchangersResponse
import com.obriylabs.currencyandroid.utils.Elector
import com.obriylabs.currencyandroid.exception.Failure
import okhttp3.ResponseBody

interface ExchangersRepository {

    fun getDatabaseDate(): Elector<Failure, DateResponse>

    fun getExchangers(filePath: String) : Elector<Failure, ResponseBody>

    fun saveExchangers(item: List<ExchangersResponse.Result>)

}