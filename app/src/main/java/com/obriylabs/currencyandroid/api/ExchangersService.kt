package com.obriylabs.currencyandroid.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * REST API access points
 */
interface ExchangersService {

    // a resource relative to your base URL
    @GET("data/updateDatabase.json") //todo or data/data.dat
    fun downloadFileWithFixedUrl(): Deferred<Response<DateResponse>>

    // using a dynamic URL
    @GET
    fun downloadFileWithDynamicUrl(@Url fileUrl: String?): Deferred<Response<ResponseBody>>

}