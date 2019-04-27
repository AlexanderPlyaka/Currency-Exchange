package com.obriylabs.currencyandroid.domain.entity

import com.google.gson.annotations.SerializedName
import com.obriylabs.currencyandroid.data.model.Exchangers

data class ExchangersEntity(
        @SerializedName("results")
        val results: List<Result>
) {
    data class Result(
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("currencyAddress")
            val currencyAddress: String,
            @SerializedName("currencyAroundOfClock")
            val currencyAroundOfClock: Int,
            @SerializedName("currencyCanSendEditRequest")
            val currencyCanSendEditRequest: Int,
            @SerializedName("currencyId")
            val currencyId: String,
            @SerializedName("currencyIsExactLocation")
            val currencyIsExactLocation: Int,
            @SerializedName("currencyLocation")
            val currencyLocation: CurrencyLocation,
            @SerializedName("currencyName")
            val currencyName: String,
            @SerializedName("currencyState")
            val currencyState: Int,
            @SerializedName("currencyWorkingTime")
            val currencyWorkingTime: String,
            @SerializedName("objectId")
            val objectId: String,
            @SerializedName("regionId")
            val regionId: Int,
            @SerializedName("updatedAt")
            val updatedAt: String
    ) {
        data class CurrencyLocation(
                @SerializedName("__type")
                val type: String,
                @SerializedName("latitude")
                val latitude: Double,
                @SerializedName("longitude")
                val longitude: Double
        )

        fun toExchangers() = Exchangers(
                createdAt,
                currencyAddress,
                currencyAroundOfClock,
                currencyCanSendEditRequest,
                currencyId,
                currencyIsExactLocation,
                currencyLocation,
                currencyName,
                currencyState,
                currencyWorkingTime,
                objectId,
                regionId,
                updatedAt)
    }
}