package com.obriylabs.currencyandroid.data.api

import com.obriylabs.currencyandroid.domain.entity.DataOfExchangersEntity
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call

/**
 * REST API access points
 */
interface ExchangersService {
    companion object {
        private const val UPDATE = "data/updateDatabase.json"
    }

    @GET(UPDATE) fun fetchDataOfExchangersEntity(): Call<DataOfExchangersEntity>

    @GET fun fetchExchangersDatabase(@Url fileUrl: String?): Call<ResponseBody>

}