package com.nowontourist.tourist.ui.home

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.FragmentHomeBinding
import com.nowontourist.tourist.databinding.ItemStampBinding
import com.nowontourist.tourist.ui.MainViewModel
import com.nowontourist.tourist.ui.dialog.StampDialog
import com.nowontourist.tourist.ui.gallery.GalleryItem
import com.nowontourist.tourist.util.firebaseDatabase
import java.util.Calendar
import java.util.Date

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var eventAdapter: EventAdapter
    private val stampDialog by lazy { StampDialog() }
    private lateinit var auth: FirebaseAuth

    private val tabsIntent by lazy { CustomTabsIntent.Builder().build() }

    private val originMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val stampList = listOf(
            binding.stamp1, binding.stamp2,
            binding.stamp3, binding.stamp4,
            binding.stamp5, binding.stamp6,
            binding.stamp7,
        )

        initMonth()
        initStamp(stampList)
        initEventAdapter()

        firebaseDatabase.collection("event").addSnapshotListener { snapshot, error ->
            if(error != null) return@addSnapshotListener

            if(snapshot != null && !snapshot.isEmpty) {
                val list = snapshot.documents.map {
                    it.toObject(Event::class.java)!!
                }
                eventAdapter.originList = list
                eventAdapter.filterByMonth(originMonth)
            }
        }

        viewModel.stamps.observe(viewLifecycleOwner) {
            stampDialog.setData(it)

            stampList.forEachIndexed { index, itemStampBinding ->
                val isStamped = it["${index + 1}"]?:false
                if(isStamped) {
                    itemStampBinding.imageStamp.visibility = View.VISIBLE
                    itemStampBinding.textStampId.text = ""
                } else {
                    itemStampBinding.imageStamp.visibility = View.INVISIBLE
                    itemStampBinding.textStampId.text = "${index + 1}"
                }
            }
        }
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
                monthList[prevIndex].backgroundTintList = yellow
                textView.backgroundTintList = orange
                prevIndex = index
                // 이벤트 목록 업데이트
                eventAdapter.filterByMonth(month)
            }
        }
    }

    private fun initStamp(stampList: List<ItemStampBinding>) {


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
                tabsIntent.launchUrl(requireContext(), Uri.parse(eventAdapter.list[position].url) )
            }
        }

        eventAdapter = EventAdapter(onClickListener).apply {
            list = listOf()
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