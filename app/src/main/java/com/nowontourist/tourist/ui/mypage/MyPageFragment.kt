package com.nowontourist.tourist.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.ListenerRegistration
import com.nowontourist.tourist.databinding.FragmentMyPageBinding
import com.nowontourist.tourist.ui.MainViewModel
import com.nowontourist.tourist.ui.auth.AuthHomeActivity
import com.nowontourist.tourist.ui.auth.InputProfileActivity
import com.nowontourist.tourist.ui.gallery.GalleryItem
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.SharedPreferencesManager
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.getUserProfileRef
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    @Inject lateinit var prefManager : SharedPreferencesManager
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val myPhotoAdapter by lazy { MyPhotoAdapter() }
    private var snapshotListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.textEdit.setOnClickListener {
            startActivity(Intent(context, InputProfileActivity::class.java))
        }

        updateUI(prefManager.getBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED))


        viewModel.userName.observe(viewLifecycleOwner) {
            binding.textName.text = it
        }

        loadProfile()
        initFirebaseListener()
    }

    private fun initFirebaseListener() {
        snapshotListener = firebaseDatabase.collection("gallery")
            .whereEqualTo(FirebaseUtil.KEY_AUTHOR, firebaseAuth.currentUser?.uid?:"Unknown")
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    return@addSnapshotListener
                }

                if(snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.map {
                        it.toObject(GalleryItem::class.java)!!
                    }
                    myPhotoAdapter.list = list
                }
            }
    }

    override fun onStart() {
        super.onStart()
        loadProfile()
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = myPhotoAdapter.apply {
            list = listOf()
        }
    }

    private fun loadProfile() {
        firebaseAuth.currentUser?.let {
            val imageRef = firebaseStorage.getUserProfileRef(firebaseAuth.currentUser?.uid)
            Glide.with(this).load(imageRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageProfile)
        }
    }

    private fun updateUI(authSkipped: Boolean) {
        if(authSkipped) {
            binding.layoutAuth.visibility = View.GONE
            binding.imageAuth.visibility = View.VISIBLE

            binding.btnAuth.text = "로그인"
            binding.btnAuth.setOnClickListener {
                prefManager.putBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED, false)
                startActivity(Intent(context, AuthHomeActivity::class.java))
                activity?.finish()
            }
        } else {
            binding.layoutAuth.visibility = View.VISIBLE
            binding.imageAuth.visibility = View.GONE

            binding.btnAuth.text = "로그아웃"
            binding.btnAuth.setOnClickListener {
                firebaseAuth.signOut()
                startActivity(Intent(context, AuthHomeActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotListener?.remove()
        _binding = null
    }
}