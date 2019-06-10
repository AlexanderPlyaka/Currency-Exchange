package com.obriylabs.currencyandroid.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters
import com.obriylabs.currencyandroid.data.room.ExchangersTypeConverter
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity

@TypeConverters(ExchangersTypeConverter::class)
@Entity(tableName = "table_of_exchangers", indices = [Index("objectId")], primaryKeys = ["objectId"])
data class Exchangers(
        @ColumnInfo(name = "created_at")
        val createdAt: String,
        @ColumnInfo(name = "currency_address")
        val currencyAddress: String,
        @ColumnInfo(name = "currency_around_of_clock")
        val currencyAroundOfClock: Int,
        @ColumnInfo(name = "currency_can_send_edit_request")
        val currencyCanSendEditRequest: Int,
        @ColumnInfo(name = "currency_id")
        val currencyId: String,
        @ColumnInfo(name = "currency_is_exact_location")
        val currencyIsExactLocation: Int,
        @ColumnInfo(name = "currency_location")
        val currencyLocation: ExchangersEntity.Result.CurrencyLocation,
        @ColumnInfo(name = "currency_name")
        val currencyName: String,
        @ColumnInfo(name = "currency_state")
        val currencyState: Int,
        @ColumnInfo(name = "currency_working_time")
        val currencyWorkingTime: String,
        val objectId: String,
        @ColumnInfo(name = "region_id")
        val regionId: Int,
        @ColumnInfo(name = "updated_at")
        val updatedAt: String
)