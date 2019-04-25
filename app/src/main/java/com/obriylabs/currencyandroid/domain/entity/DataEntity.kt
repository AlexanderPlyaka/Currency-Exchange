package com.obriylabs.currencyandroid.domain.entity

import com.google.gson.annotations.SerializedName

data class DataEntity(
        @SerializedName("date")
        val date: String,
        @SerializedName("filePath")
        val filePath: String
) {

    companion object {
        fun empty() = DataEntity("", "")
    }

}