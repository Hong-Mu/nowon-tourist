package com.nowontourist.tourist.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.DialogInfoBinding

class InfoDialog: DialogFragment() {

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    private var stamp: Stamp? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }
    }

    fun setData(stamp: Stamp) {
        this.stamp = stamp
    }

    override fun onStart() {
        super.onStart()
        stamp?.let {
            binding.textTitle.text = it.title
            binding.textContent.text = it.description
            Glide.with(binding.root)
                .load(it.imageUrl)
                .into(binding.imageMain)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}