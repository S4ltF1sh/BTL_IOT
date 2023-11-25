package com.example.btliot.view.home.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.btliot.MyApp
import com.example.btliot.database.LogDao
import com.example.btliot.model.LogData

class LogViewModel(private val mLogRepository: LogDao) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LogViewModel(MyApp.getInstance().database.logDao()) as T
            }
        }
    }

    fun allLogs() = mLogRepository.getAll()

    fun insert(log: LogData) = mLogRepository.insert(log)

    fun update(log: LogData) = mLogRepository.update(log)

    fun delete(log: LogData) = mLogRepository.delete(log)
}