package com.example.btliot.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.btliot.model.LogData
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query("SELECT * FROM log_table ORDER BY time DESC")
    fun getAll(): Flow<List<LogData>>

    @Query("SELECT * FROM log_table WHERE id = :id")
    fun getById(id: Int): LogData

    @Query("SELECT * FROM log_table WHERE time = :time")
    fun getByTime(time: Long): LogData

    @Query("SELECT * FROM log_table WHERE content = :content")
    fun getByContent(content: String): LogData

    @Query("SELECT * FROM log_table WHERE is_from_firebase = :isFromFirebase")
    fun getByIsFromFirebase(isFromFirebase: Boolean): LogData

    @Query("UPDATE log_table SET time = :time WHERE id = :id")
    fun updateTime(id: Int, time: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(log: LogData): Long

    @Update
    fun update(log: LogData)

    @Delete
    fun delete(log: LogData)
}