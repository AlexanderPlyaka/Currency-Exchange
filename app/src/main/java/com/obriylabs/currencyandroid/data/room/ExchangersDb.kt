package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.DataOfExchangers

@Database(entities = [Exchangers::class, DataOfExchangers::class], version = 1, exportSchema = false)
abstract class ExchangersDb : RoomDatabase() {

    abstract fun exchangersDao(): ExchangersDao

}