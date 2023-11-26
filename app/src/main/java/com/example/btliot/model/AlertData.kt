package com.example.btliot.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertData(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo
    val time: Long = System.currentTimeMillis(),

    @ColumnInfo
    val userLocation: String,

    @ColumnInfo(name = "is_fire")
    val isFire: Boolean
)