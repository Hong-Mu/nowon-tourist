package com.nowontourist.tourist.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.FragmentHomeBinding
import com.nowontourist.tourist.ui.stamp.StampDialog
import java.util.Calendar
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val stampDialog by lazy { StampDialog() }
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        initMonth()
        initStamp()
        initEventAdapter()
    }

    private fun initMonth() {
        var calendar = Calendar.getInstance()
        val monthList = listOf(
            binding.textMonth1, binding.textMonth2,
            binding.textMonth3, binding.textMonth4,
            binding.textMonth5,
        )

        var prevIndex = 0
        val yellow = ColorStateList.valueOf(resources.getColor(R.color.yellow))
        val orange = ColorStateList.valueOf(resources.getColor(R.color.orange))
        monthList.forEachIndexed { index, textView ->
            val month = calendar.get(Calendar.MONTH) + 1
            calendar.add(Calendar.MONTH, 1)
            textView.text = "${month}월"

            textView.setOnClickListener {
                prevIndex = index
                monthList[prevIndex].backgroundTintList = yellow
                textView.backgroundTintList = orange
                // 이벤트 목록 업데이트
            }
        }
    }

    private fun initStamp() {
        val stampList = listOf(
            binding.stamp1, binding.stamp2,
            binding.stamp3, binding.stamp4,
            binding.stamp5, binding.stamp6,
            binding.stamp7,
        )

        stampList.forEachIndexed { index, itemStampBinding ->
            itemStampBinding.textStampId.text = "${index + 1}"
        }

        binding.layoutStamp.setOnClickListener {
            activity?.run {
                stampDialog.show(supportFragmentManager, ".HomeFragment")
            }
        }
    }

    private fun initEventAdapter() {
        val onClickListener = object : EventAdapter.OnItemClickListener {
            override fun onClick(position: Int) {

            }
        }

        eventAdapter = EventAdapter(onClickListener).apply {
            list = listOf(
                Event("빛조각 페스티벌", "설명", "https://url.com", Date(), Date(), "당현천 산택길"),
                Event("빛조각 페스티벌", "설명", "https://url.com", Date(), Date(), "당현천 산택길"),
                Event("빛조각 페스티벌", "설명", "https://url.com", Date(), Date(), "당현천 산택길"),
                Event("빛조각 페스티벌", "설명", "https://url.com", Date(), Date(), "당현천 산택길"),
                Event("빛조각 페스티벌", "설명", "https://url.com", Date(), Date(), "당현천 산택길"),
            )
        }

        binding.rvEvent.adapter = eventAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}