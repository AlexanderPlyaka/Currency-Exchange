package com.obriylabs.currencyandroid.data.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity

@Dao
interface ExchangersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exchangers: List<Exchangers>)

    @Query("SELECT * FROM Exchangers")
    fun getAll(): List<Exchangers>

    @Query("SELECT * FROM Exchangers WHERE objectId = :id")
    fun getById(id: Long): Exchangers

}