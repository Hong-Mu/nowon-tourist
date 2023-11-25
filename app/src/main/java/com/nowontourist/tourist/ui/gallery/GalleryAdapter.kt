package com.nowontourist.tourist.ui.gallery

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.ItemGalleryBinding
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.getGalleryRef

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>(){
    var list: List<GalleryItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(private val binding: ItemGalleryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryItem) {
            val docRef = firebaseDatabase.document("gallery/${item.id}")

            Glide.with(binding.root)
                .load(firebaseStorage.getGalleryRef(item.id))
                .into(binding.imageMain)

            val orangeState = ColorStateList.valueOf(binding.root.context.getColor(R.color.orange))
            val grayState = ColorStateList.valueOf(binding.root.context.getColor(R.color.gray))

            val likes = item.likes?.size?:0
            binding.textLikes.text = likes.toString()

            if(item.likes?.contains(firebaseAuth.currentUser?.uid?:"") == true) {
                binding.imageLikes.setImageResource(R.drawable.ic_heart_fill)
                binding.imageLikes.imageTintList = orangeState
                binding.textLikes.setTextColor(orangeState)
            } else {
                binding.imageLikes.setImageResource(R.drawable.ic_heart_blank)
                binding.imageLikes.imageTintList = grayState
                binding.textLikes.setTextColor(grayState)
            }

            binding.btnLike.setOnClickListener {
                if(firebaseAuth.currentUser != null) {
                    if(item.likes?.contains(firebaseAuth.currentUser!!.uid) == true) {
                        docRef.update("likes", FieldValue.arrayRemove(firebaseAuth.currentUser!!.uid))
                    } else {
                        docRef.update("likes", FieldValue.arrayUnion(firebaseAuth.currentUser!!.uid))
                    }
                } else {
                    // TODO: 로그인 필요
                }
            }
        }
    }
    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position) }
    }
}