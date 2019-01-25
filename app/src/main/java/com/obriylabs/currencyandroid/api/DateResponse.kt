package com.obriylabs.currencyandroid.api

import com.google.gson.annotations.SerializedName

data class DateResponse(
        @SerializedName("date")
        val date: String,
        @SerializedName("filePath")
        val filePath: String
)