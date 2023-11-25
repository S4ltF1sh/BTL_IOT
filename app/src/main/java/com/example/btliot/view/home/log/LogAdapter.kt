package com.example.btliot.view.home.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.btliot.R
import com.example.btliot.databinding.ItemLogBinding
import com.example.btliot.model.LogData

class LogAdapter : ListAdapter<LogData, LogAdapter.LogViewHolder>(LogDiffCallback()) {
    inner class LogViewHolder(private val mBinding: ItemLogBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(log: LogData) {
            mBinding.apply {
                tvTime.text = log.time.toString()
                tvContent.text = log.content
                imvIconType.setBackgroundResource(if (log.isFromFirebase) R.drawable.ic_from_cloud else R.drawable.ic_to_cloud)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LogViewHolder(ItemLogBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = currentList[position]
        holder.bind(log)
    }
}