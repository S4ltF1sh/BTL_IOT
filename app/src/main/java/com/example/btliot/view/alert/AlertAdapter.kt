package com.example.btliot.view.alert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.btliot.R
import com.example.btliot.databinding.ItemAlertBinding
import com.example.btliot.model.AlertData

class AlertAdapter(private val onItemClick: (alertData: AlertData) -> Unit) :
    ListAdapter<AlertData, AlertAdapter.AlertViewHolder>(AlertDiffCallback()) {
    inner class AlertViewHolder(private val mBinding: ItemAlertBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(alert: AlertData) {
            mBinding.apply {
                tvTime.text = alert.time.toString()
                tvContent.text = alert.userLocation
                imvIconType.setBackgroundResource(if (alert.isFire) R.drawable.ic_fire else R.drawable.ic_gas)

                root.setOnClickListener {
                    onItemClick(alert)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AlertViewHolder(ItemAlertBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val log = currentList[position]
        holder.bind(log)
    }
}