package com.example.btliot.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_table")
data class LogData(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo
    val time: Long = System.currentTimeMillis(),

    @ColumnInfo
    val content: String,

    @ColumnInfo(name = "is_from_firebase")
    val isFromFirebase: Boolean
)
