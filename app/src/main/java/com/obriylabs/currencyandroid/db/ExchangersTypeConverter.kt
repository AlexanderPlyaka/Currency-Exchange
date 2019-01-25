package com.obriylabs.currencyandroid.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.obriylabs.currencyandroid.api.ExchangersResponse

class ExchangersTypeConverter {

    @TypeConverter
    fun toTorrent(json: String): ExchangersResponse.Result.CurrencyLocation {
        val type = object : TypeToken<ExchangersResponse.Result.CurrencyLocation>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(torrent: ExchangersResponse.Result.CurrencyLocation): String {
        val type = object : TypeToken<ExchangersResponse.Result.CurrencyLocation>() {}.type
        return Gson().toJson(torrent, type)
    }
}