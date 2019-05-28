package com.obriylabs.currencyandroid.data.model

import okhttp3.ResponseBody

data class ReceivedExchangers(val responseBody: ResponseBody) {

    companion object {
        fun empty(): ResponseBody = ResponseBody.create(null, byteArrayOf())
    }

}