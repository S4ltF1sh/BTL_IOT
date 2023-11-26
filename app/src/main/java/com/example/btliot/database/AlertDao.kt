package com.example.btliot.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.btliot.model.AlertData
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alert_table ORDER BY time DESC")
    fun getAll(): Flow<List<AlertData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(log: AlertData): Long

    @Update
    fun update(log: AlertData)

    @Delete
    fun delete(log: AlertData)
}