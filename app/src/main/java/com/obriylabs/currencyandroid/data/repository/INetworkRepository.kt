package com.obriylabs.currencyandroid.data.repository

import com.obriylabs.currencyandroid.data.room.Exchangers
import com.obriylabs.currencyandroid.domain.ReceivedData
import com.obriylabs.currencyandroid.domain.entity.DataEntity
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure

interface INetworkRepository {

    fun data() : Result<Failure, DataEntity>

    fun exchangers(filePath: String) : Result<Failure, ReceivedData>

    fun fileHandler(byteArray: ByteArray) : Result<Failure, ExchangersEntity>

    fun saveToDb(items: List<Exchangers>)

    fun fetchFromDb() : Result<Failure, List<Exchangers>>

}