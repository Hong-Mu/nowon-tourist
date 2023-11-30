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

    private var _binding: DialogStampBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { StampBoxAdapter() }
    private val infoDialog by lazy { InfoDialog() }
    private var stampMap: Map<String, Boolean>? = null

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
            list = listOf(
                Stamp(1, "노원 불빛정원", "https://www.nowon.kr/resources/www/images/info/img-nowon_light07.gif", stampMap?.get("1")?:false),
                Stamp(2, "아바타트리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc07.gif", stampMap?.get("2")?:false),
                Stamp(3, "경춘선숲길 갤러리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc06.gif", stampMap?.get("3")?:false),
                Stamp(4, "Cafe 기차가 있는 풍경", "https://www.nowon.kr/resources/www/images/info/img-healing-gc12.gif", stampMap?.get("4")?:false),
                Stamp(5, "타임뮤지엄", "https://www.nowon.kr/resources/www/images/info/img-healing-gc13.gif", stampMap?.get("5")?:false),
                Stamp(6, "노원기차마을", "https://www.nowon.kr/resources/www/images/info/img-healing-gc16.gif", stampMap?.get("6")?:false),
                Stamp(7, "경춘선철길", "https://www.nowon.kr/resources/www/images/info/img-healing-gc10.gif", stampMap?.get("7")?:false),
            )
        }

        adapter.setOnItemClickListener {

            infoDialog.show(parentFragmentManager, "info")
            infoDialog.setData(Info(
                "제목",
                "장소",
                "설명",
                "https://cdn.pixabay.com/photo/2023/11/10/17/00/mountains-8379756_640.jpg",

                ))
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
        this.stampMap = map
        adapter.list = listOf(
            Stamp(1, "노원 불빛정원", "https://www.nowon.kr/resources/www/images/info/img-nowon_light07.gif", stampMap?.get("1")?:false),
            Stamp(2, "아바타트리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc07.gif", stampMap?.get("2")?:false),
            Stamp(3, "경춘선숲길 갤러리", "https://www.nowon.kr/resources/www/images/info/img-healing-gc06.gif", stampMap?.get("3")?:false),
            Stamp(4, "Cafe 기차가 있는 풍경", "https://www.nowon.kr/resources/www/images/info/img-healing-gc12.gif", stampMap?.get("4")?:false),
            Stamp(5, "타임뮤지엄", "https://www.nowon.kr/resources/www/images/info/img-healing-gc13.gif", stampMap?.get("5")?:false),
            Stamp(6, "노원기차마을", "https://www.nowon.kr/resources/www/images/info/img-healing-gc16.gif", stampMap?.get("6")?:false),
            Stamp(7, "경춘선철길", "https://www.nowon.kr/resources/www/images/info/img-healing-gc10.gif", stampMap?.get("7")?:false),
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