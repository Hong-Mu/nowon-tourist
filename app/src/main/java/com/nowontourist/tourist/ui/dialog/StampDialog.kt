package com.nowontourist.tourist.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.nowontourist.tourist.databinding.DialogStampBinding
import com.nowontourist.tourist.ui.home.Event
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.uploadStamp

class StampDialog: DialogFragment() {

    companion object {
        var stampMap: Map<String, Boolean>? = null
    }

    private var _binding: DialogStampBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { StampBoxAdapter() }
    private val infoDialog by lazy { InfoDialog() }

    private var imageUri: Uri? = null
    private var stampId = 0
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                imageUri = result.data?.data
                uploadImage(imageUri)
            }
        }

    private fun uploadImage(uri: Uri?) {
        uri?.let {
            binding.progressBar.visibility = View.VISIBLE
            firebaseStorage.uploadStamp(firebaseAuth.currentUser?.uid, stampId, it)
                .addOnSuccessListener {
                    val ref = firebaseDatabase.collection(FirebaseUtil.COLLECTION_USERS)
                        .document(firebaseAuth.currentUser?.uid!!)

                    ref.update("${FirebaseUtil.KEY_STAMPS}.$stampId", true)
                        .addOnCompleteListener {
                            binding.progressBar.visibility = View.GONE
                        }
                }.addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }

        binding.rvStamp.adapter = adapter.apply {
            list = getList()
        }

        adapter.setOnItemClickListener { position ->

            infoDialog.show(parentFragmentManager, "info")
            infoDialog.setData(adapter.list[position])
        }

        adapter.setOnStampButtonClickListener {  position ->

            this.stampId = position + 1
            imageLauncher.launch(Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            })
        }
    }

    fun setData(map: Map<String, Boolean>) {
        stampMap = map
        adapter.list = getList()
    }

    private fun getList(): List<Stamp> {
        return listOf(
            Stamp(1, "옛 경춘선 철길구간을 고즈넉한 경춘선숲길로 바꾸고 공원으로 꾸민 화랑대역 철도공원에 야간 경관 조성으로 빛의 향연이 펼쳐지는 서울시 최초 불빛정원입니다. 누구나 편안하게 찾아오셔서 즐기실 수 있는 힐링공간입니다.","노원 불빛정원", "https://www.nowon.kr/resources/www/images/info/img-nowon_light07.gif", stampMap?.get("1")?:false),
            Stamp(2, "", "아바타트리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc07.gif", stampMap?.get("2")?:false),
            Stamp(3, "", "경춘선숲길 갤러리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc06.gif", stampMap?.get("3")?:false),
            Stamp(4, "장인이 제작한 미니기차가 음료를 자리까지 배달해주는 기차 테마 카페, ‘카페 기차가 있는 풍경’은 전문가가 직접 로스팅한 스페셜티 커피를 제공합니다.", "Cafe 기차가 있는 풍경", "https://www.nowon.kr/resources/www/images/info/img-healing-gc12.gif", stampMap?.get("4")?:false),
            Stamp(5, "기차여행의 추억이 있는 화랑대역사공원 내에 시간여행이라는 주제로 만들어진 공간입니다.\n\n" +
                    "시간측정을 위한 인류의 장구한 노력의 여정과 시계를 예술품으로 다듬어낸 시계장인들의 중세~현대시계 전시를 통해, 시간에 대해 생각하고 시간의 소중함을 느끼고 경험하는 기회가 될 것입니다.\n\n" +
                    "옛 경춘선의 추억도 살리고 산책로도 제공하는 낭만적인 공간으로 안내합니다.\n\n" +
                    "위치 : 공릉동 29-4 일대\n\n주요시설 : 매표소, 전시실, 기념품판매점\n\n개관시간 : 매일 10시 ~ 19시(입장마감 : 18시) ※ 매주 월요일, 설날·추석 당일 휴관\n\n", "타임뮤지엄", "https://www.nowon.kr/resources/www/images/info/img-healing-gc13.gif", stampMap?.get("5")?:false),
            Stamp(6, "화랑대철도공원에 스위스의 아름다운 자연풍광과 철도마을을 보고 체험할 수 있는 미니어처 전시관이 조성되었습니다.", "노원기차마을", "https://www.nowon.kr/resources/www/images/info/img-healing-gc16.gif", stampMap?.get("6")?:false),
            Stamp(7, "", "경춘선철길", "https://www.nowon.kr/resources/www/images/info/img-healing-gc10.gif", stampMap?.get("7")?:false),
        )
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