package com.obriylabs.currencyandroid.api

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.obriylabs.currencyandroid.db.ExchangersTypeConverter


data class ExchangersResponse(
        @SerializedName("results")
        val results: List<Result>
) {

    @TypeConverters(ExchangersTypeConverter::class)
    @Entity(
            indices = [Index("objectId")],
            primaryKeys = ["objectId"])
    data class Result(
            @field:SerializedName("createdAt")
            val createdAt: String,
            @field:SerializedName("currencyAddress")
            val currencyAddress: String,
            @field:SerializedName("currencyAroundOfClock")
            val currencyAroundOfClock: Int,
            @field:SerializedName("currencyCanSendEditRequest")
            val currencyCanSendEditRequest: Int,
            @field:SerializedName("currencyId")
            val currencyId: String,
            @field:SerializedName("currencyIsExactLocation")
            val currencyIsExactLocation: Int,
            @field:SerializedName("currencyLocation")
            val currencyLocation: CurrencyLocation,
            @field:SerializedName("currencyName")
            val currencyName: String,
            @field:SerializedName("currencyState")
            val currencyState: Int,
            @field:SerializedName("currencyWorkingTime")
            val currencyWorkingTime: String,
            @field:SerializedName("objectId")
            val objectId: String,
            @field:SerializedName("regionId")
            val regionId: Int,
            @field:SerializedName("updatedAt")
            val updatedAt: String
    ) {
        data class CurrencyLocation(
                @field:SerializedName("__type")
                val type: String,
                @field:SerializedName("latitude")
                val latitude: Double,
                @field:SerializedName("longitude")
                val longitude: Double
        )
    }
}