package com.nowontourist.tourist.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nowontourist.tourist.databinding.DialogInfoBinding

class InfoDialog: DialogFragment() {

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    private var info: Info? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }
    }

    fun setData(info: Info) {
        this.info = info
    }

    override fun onStart() {
        super.onStart()
        info?.let {
            binding.textTitle.text = it.title
            binding.textContent.text = it.description
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