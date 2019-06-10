package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.data.model.DataOfExchangers
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.ExchangersResult
import com.obriylabs.currencyandroid.data.model.ReceivedExchangers
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure

interface IExchangersRepository {

    fun fetchDataOfExchangers() : Result<Failure, DataOfExchangers>

    fun fetchExchangers(filePath: String) : Result<Failure, ReceivedExchangers>

    fun exchangersFileHandler(byteArray: ByteArray) : Result<Failure, ExchangersResult>

    fun saveDataOfExchangersToDb(dataOfExchangers: DataOfExchangers)

    fun saveExchangersToDb(items: List<Exchangers>)

    fun fetchDateFromDb(data: String): Result<Failure, DataOfExchangers>

    fun fetchExchangersFromDb() : Result<Failure, List<Exchangers>>

}