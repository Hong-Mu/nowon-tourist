package com.nowontourist.tourist.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.FragmentMapBinding
import com.nowontourist.tourist.ui.dialog.StampDialog

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap

    private lateinit var townView: View
    private val townBottomSheet by lazy { BottomSheetDialog(requireContext()) }

    private val stampDialog by lazy { StampDialog() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.btnStamp.setOnClickListener { stampDialog.show(parentFragmentManager, "stamp") }

        initTown()
    }

    private fun initTown() {
        townView = layoutInflater.inflate(R.layout.dialog_town, null)


        val adapter = TownAdapter().apply {
            list = listOf(
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
                Town(0, "노원 불빛정원", "당현척 산책길"),
            )
        }

        townView.findViewById<RecyclerView>(R.id.rv_town).adapter = adapter

        townBottomSheet.setContentView(townView)

        binding.btnTown.setOnClickListener {
            townBottomSheet.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}