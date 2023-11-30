package com.nowontourist.tourist.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.ItemStampBoxBinding

class StampBoxAdapter: RecyclerView.Adapter<StampBoxAdapter.ViewHolder>() {
    var list: List<Stamp> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: (Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    private var onStampButtonClickListener: ((Int) -> Unit)? = null
    fun setOnStampButtonClickListener(listener: (Int) -> Unit) {
        this.onStampButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStampBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position) }
    }

    inner class ViewHolder(val binding: ItemStampBoxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Stamp, position: Int) {
            binding.textTitle.text = item.title

            Glide.with(binding.imagePreview)
                .load(item.imageUrl)
                .into(binding.imagePreview)

            bindStamp(item)

            binding.btnStamp.setOnClickListener { onStampButtonClickListener?.invoke(position) }
        }

        private fun bindStamp(item: Stamp){
            if(item.isStamped) {
                binding.stamp.root.visibility = View.VISIBLE
                binding.stamp.imageStamp.visibility = View.VISIBLE
                binding.btnStamp.visibility = View.INVISIBLE
                binding.stamp.textStampId.text = ""
            } else {
                binding.stamp.root.visibility = View.GONE
            }
        }
    }
}