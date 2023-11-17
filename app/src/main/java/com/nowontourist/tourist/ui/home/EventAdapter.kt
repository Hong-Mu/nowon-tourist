package com.nowontourist.tourist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowontourist.tourist.databinding.ItemEventBinding

class EventAdapter(val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    var list: List<Event> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { onItemClickListener.onClick(position) }
    }

    inner class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.textTitle.text = item.title
            binding.textPeriod.text = item.startDate.toString()
            binding.textLocation.text = item.location
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}