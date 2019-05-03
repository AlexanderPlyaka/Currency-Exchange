package com.obriylabs.currencyandroid.domain.entity

import com.google.gson.annotations.SerializedName
import com.obriylabs.currencyandroid.data.model.DataOfExchangers

data class DataExchangersEntity(
        @SerializedName("date")
        val date: String,
        @SerializedName("filePath")
        val filePath: String
) {

    companion object {
        fun empty() = DataExchangersEntity("", "")
    }

    fun toDataOfExchangers() = DataOfExchangers(date, filePath)

}