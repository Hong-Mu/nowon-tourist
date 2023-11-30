package com.nowontourist.tourist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowontourist.tourist.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    var list: List<Event> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var originList: List<Event> = listOf()

    private val format = SimpleDateFormat("MM.dd(E)", Locale.getDefault())

    fun filterByMonth(month: Int) { // TODO 상당히 조악하지만 그냥 간다
        val temp = originList
            .filter { it.startDate?.month?.plus(1) == month || it.endDate?.month?.plus(1) == month}
            .sortedBy { it.startDate }

        list = temp
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
            binding.textPeriod.text = "${format.format(item.startDate)}-${format.format(item.endDate)}"
            binding.textLocation.text = item.location
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}