package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity

@Database(entities = [Exchangers::class], version = 1, exportSchema = false)
abstract class ExchangersDb : RoomDatabase() {

    abstract fun exchangersDao(): ExchangersDao
}