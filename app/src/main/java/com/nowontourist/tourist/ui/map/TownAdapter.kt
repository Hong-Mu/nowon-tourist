package com.nowontourist.tourist.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.ItemTownBinding

class TownAdapter: RecyclerView.Adapter<TownAdapter.ViewHolder>() {
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
                .load("https://cdn.pixabay.com/photo/2023/11/17/22/10/road-8395119_1280.jpg")
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
    }
}