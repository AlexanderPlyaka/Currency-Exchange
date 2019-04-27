package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.obriylabs.currencyandroid.data.model.SourceDb
import android.arch.persistence.room.Delete

@Dao
interface ExchangersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllExchangers(exchangers: List<Exchangers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSource(sourceDb: SourceDb)

    @Query("SELECT * FROM table_of_exchangers")
    fun getAllExchangers(): List<Exchangers>

    @Query("SELECT * FROM table_of_source WHERE date = :date")
    fun getDate(date: String): SourceDb

    @Delete
    fun deleteExchangers(exchangers: List<Exchangers>)

    @Delete
    fun deleteSourceDb(sourceDb: SourceDb)

}