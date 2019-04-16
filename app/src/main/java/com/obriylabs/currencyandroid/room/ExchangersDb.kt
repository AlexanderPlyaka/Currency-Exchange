package com.obriylabs.currencyandroid.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.obriylabs.currencyandroid.api.ExchangersResponse

@Database(entities = [ExchangersResponse.Result::class], version = 1, exportSchema = false)
abstract class ExchangersDb : RoomDatabase() {

    abstract fun exchangersDao(): ExchangersDao
}