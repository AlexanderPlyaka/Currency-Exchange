package com.obriylabs.currencyandroid.data.storage

import com.obriylabs.currencyandroid.domain.Result
import com.obriylabs.currencyandroid.domain.exception.Failure

interface IFileHandler<T> {

    fun dataProcess(bytes: ByteArray?) : Result<Failure, T>

}