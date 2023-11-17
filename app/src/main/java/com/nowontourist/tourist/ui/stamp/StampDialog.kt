package com.nowontourist.tourist.ui.stamp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nowontourist.tourist.databinding.DialogStampBinding
import com.nowontourist.tourist.ui.home.Event

class StampDialog: DialogFragment() {

    private var _binding: DialogStampBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { StampBoxAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }

        binding.rvStamp.adapter = adapter.apply {
            list = listOf(
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/11/10/17/00/mountains-8379756_640.jpg", true),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/11/12/15/42/flowers-8383322_640.jpg", true),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/09/09/12/38/fisherman-8243136_640.jpg", false),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/09/16/18/18/wallpaper-8257343_640.png", true),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/10/28/09/20/darling-8346954_640.jpg", false),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/10/20/13/49/beach-8329531_640.jpg", true),
                Stamp(1, "제목1", "https://cdn.pixabay.com/photo/2023/11/08/20/11/mountains-8375693_1280.jpg", true),
            )
        }
    }

    fun setItem(item: Event) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}