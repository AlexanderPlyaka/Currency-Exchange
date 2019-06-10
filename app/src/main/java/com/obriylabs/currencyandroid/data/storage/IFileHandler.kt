package com.obriylabs.currencyandroid.data.storage

interface IFileHandler<T> {

    fun dataProcess(bytes: ByteArray?) : T

}