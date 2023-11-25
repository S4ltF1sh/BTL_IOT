package com.example.btliot.view.home.log

import androidx.recyclerview.widget.DiffUtil
import com.example.btliot.model.LogData

class LogDiffCallback : DiffUtil.ItemCallback<LogData>() {
    override fun areItemsTheSame(oldItem: LogData, newItem: LogData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LogData, newItem: LogData): Boolean {
        return oldItem == newItem
    }
}