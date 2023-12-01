package com.nowontourist.tourist.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.btnStamp.setOnClickListener { stampDialog.show(parentFragmentManager, "stamp") }

        initTown()
    }

    private fun initTown() {
        townView = layoutInflater.inflate(R.layout.dialog_town, null)


        val adapter = TownAdapter(object: TownAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                townBottomSheet.dismiss()
                val list = getTownList()
                val latLng = LatLng(list[position].latitude, list[position].longitude)
                val position = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(17f)
                    .build()

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))

            }
        }).apply {
            list = getTownList()
        }

        townView.findViewById<RecyclerView>(R.id.rv_town).adapter = adapter

        townBottomSheet.setContentView(townView)

        binding.btnTown.setOnClickListener {
            townBottomSheet.show()
        }
    }

    private fun getTownList(): List<Town> {
        return listOf(
            Town(0, "정원지원센터", "불암산 힐링타운", "https://www.nowon.kr/resources/www/images/info/img-ad-garden9.gif", 37.6554, 127.0809),
            Town(0, "산림치유센터", "불암산 힐링타운", "https://mediahub.seoul.go.kr/uploads/mediahub/2021/08/OmgQfgkEvVFiNenmCgqXpFpRrXQIDwcX.png", 37.6556, 127.0807),
            Town(0, "노원 불빛정원", "경춘선 힐링타운", "https://www.nowon.kr/resources/www/images/info/img-healing-gc04.gif", 37.624144, 127.091882),
            Town(0, "경춘선숲길갤러리", "경춘선 힐링타운", "https://www.nowon.kr/resources/www/images/info/img-healing-gc06.gif", 37.624950, 127.093755),
            Town(0, "Cafe 기차가 있는 풍경", "경춘선 힐링타운", "https://www.nowon.kr/resources/www/images/info/img-healing-gc11.gif", 37.623922, 127.092792),
            Town(0, "타임뮤지엄", "경춘선 힐링타운", "https://www.nowon.kr/resources/www/images/info/img-healing-gc14.gif", 37.624510, 127.093098),
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(37.624950, 127.093755)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        getTownList().forEach {
            mMap.addMarker(MarkerOptions()
                .position(LatLng(it.latitude, it.longitude))
                .title(it.name)
            )
        }
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