package com.obriylabs.currencyandroid.data.model

import android.arch.persistence.room.Entity

@Entity(tableName = "table_of_source", primaryKeys = ["date"])
data class SourceDb(val date: String, val filePath: String)