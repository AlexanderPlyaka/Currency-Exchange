package com.obriylabs.currencyandroid.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call

/**
 * REST API access points
 */
interface ExchangersService {

    // a resource relative to your base URL
    @GET("data/updateDatabase.json")
    fun downloadFileWithFixedUrl(): Call<DateResponse>

    // using a dynamic URL
    @GET
    fun downloadFileWithDynamicUrl(@Url fileUrl: String?): Call<ResponseBody>

}