package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity

class ExchangersTypeConverter {

    @TypeConverter
    fun fromJson(json: String): ExchangersEntity.Result.CurrencyLocation {
        val type = object : TypeToken<ExchangersEntity.Result.CurrencyLocation>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(data: ExchangersEntity.Result.CurrencyLocation): String {
        val type = object : TypeToken<ExchangersEntity.Result.CurrencyLocation>() {}.type
        return Gson().toJson(data, type)
    }
}