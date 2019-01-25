package com.obriylabs.currencyandroid.db

import android.arch.lifecycle.LiveData
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
    fun loadExchangers(): LiveData<List<ExchangersResponse.Result>>

}