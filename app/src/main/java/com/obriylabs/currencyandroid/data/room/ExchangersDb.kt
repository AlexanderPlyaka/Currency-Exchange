package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.SourceDb

@Database(entities = [Exchangers::class, SourceDb::class], version = 1, exportSchema = false)
abstract class ExchangersDb : RoomDatabase() {

    abstract fun exchangersDao(): ExchangersDao

}