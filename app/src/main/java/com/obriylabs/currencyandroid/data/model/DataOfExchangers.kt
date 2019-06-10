package com.obriylabs.currencyandroid.data.model

import android.arch.persistence.room.Entity

@Entity(tableName = "table_data_of_exchangers", primaryKeys = ["date"])
data class DataOfExchangers(val date: String, val filePath: String)