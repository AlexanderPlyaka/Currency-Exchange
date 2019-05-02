package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.InputData
import com.obriylabs.currencyandroid.data.model.SourceDb
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure

interface INetworkRepository {

    fun source() : Result<Failure, SourceDb>

    fun exchangers(filePath: String) : Result<Failure, InputData>

    fun fileHandler(byteArray: ByteArray) : Result<Failure, ExchangersEntity>

    fun saveExchangersToDb(items: List<Exchangers>)

    fun saveSourceToDb(sourceDb: SourceDb)

    fun fetchExchangersFromDb() : Result<Failure, List<Exchangers>>

    fun fetchDateFromDb(data: String): Result<Failure, SourceDb>

}