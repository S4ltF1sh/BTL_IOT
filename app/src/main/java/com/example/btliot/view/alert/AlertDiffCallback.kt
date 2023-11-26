package com.example.btliot.view.alert

import androidx.recyclerview.widget.DiffUtil
import com.example.btliot.model.AlertData

class AlertDiffCallback : DiffUtil.ItemCallback<AlertData>() {
    override fun areItemsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return oldItem == newItem
    }
}