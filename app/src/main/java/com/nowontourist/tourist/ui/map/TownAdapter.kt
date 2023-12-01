package com.nowontourist.tourist.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.ItemTownBinding

class TownAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<TownAdapter.ViewHolder>() {
    var list: List<Town> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemTownBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(town: Town) {
            binding.textTitle.text = town.name
            binding.textDescription.text = town.description

            Glide.with(binding.root)
                .load(town.image)
                .into(binding.imageTown)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}