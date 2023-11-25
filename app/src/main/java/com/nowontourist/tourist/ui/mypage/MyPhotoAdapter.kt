package com.nowontourist.tourist.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.ItemMyPhotoBinding

class MyPhotoAdapter() : RecyclerView.Adapter<MyPhotoAdapter.ViewHolder>() {
    var list = listOf<MyPhoto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemMyPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPhoto) {
            Glide.with(binding.root.context).load(item.photoUrl).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUrl = list[position]
        holder.bind(photoUrl)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}