package com.nowontourist.tourist.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//TODO : 갤러리 완성되면 바인딩 하는 코드 수정
class GalleryAdapter(private val photoList: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemGalleryPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUrl: String) {
            Glide.with(binding.root.context).load(photoUrl).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUrl = photoList[position]
        holder.bind(photoUrl)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}