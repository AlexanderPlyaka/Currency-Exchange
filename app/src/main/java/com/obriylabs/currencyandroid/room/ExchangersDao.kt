package com.obriylabs.currencyandroid.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.obriylabs.currencyandroid.api.ExchangersResponse

@Dao
interface ExchangersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exchangers: List<ExchangersResponse.Result>)

    @Query("SELECT * FROM Result")
    fun getExchangers(): List<ExchangersResponse.Result>

}