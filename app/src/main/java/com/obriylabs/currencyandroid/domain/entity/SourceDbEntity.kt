package com.obriylabs.currencyandroid.domain.entity

import com.google.gson.annotations.SerializedName
import com.obriylabs.currencyandroid.data.model.SourceDb

data class SourceDbEntity(
        @SerializedName("date")
        val date: String,
        @SerializedName("filePath")
        val filePath: String
) {

    companion object {
        fun empty() = SourceDbEntity("", "")
    }

    fun toSourceDb() = SourceDb(date, filePath)

}