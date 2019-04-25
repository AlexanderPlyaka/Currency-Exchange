package com.obriylabs.currencyandroid.data.storage

import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure

interface IFileHandler {

    fun getExchangers(bytes: ByteArray?) : Result<Failure, ExchangersEntity>

}