package com.obriylabs.currencyandroid.domain.entity

import com.google.gson.annotations.SerializedName
import com.obriylabs.currencyandroid.data.model.DataOfExchangers

data class DataOfExchangersEntity(
        @SerializedName("date")
        val date: String,
        @SerializedName("filePath")
        val filePath: String
) {

    companion object {
        fun empty() = DataOfExchangersEntity("", "")
    }

    fun toDataOfExchangers() = DataOfExchangers(date, filePath)

}