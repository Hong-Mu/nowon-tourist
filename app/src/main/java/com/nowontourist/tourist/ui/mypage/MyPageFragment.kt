package com.nowontourist.tourist.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nowontourist.tourist.databinding.FragmentMyPageBinding
import com.nowontourist.tourist.ui.auth.AuthHomeActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAuthButton()
        loadProfile()
    }

    private fun loadProfile() {
        firebaseAuth.currentUser?.let {
            firebaseDatabase.collection(FirebaseUtil.COLLECTION_USERS)
                .document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    val name = document.getString(FirebaseUtil.KEY_NAME)
                    // val email = document.getString(FirebaseUtil.KEY_EMAIL)
                    binding.textName.text = name
                }.addOnFailureListener { exception ->
                    // TODO: Handle error
                }

            val imageRef = firebaseStorage.getUserProfileRef(firebaseAuth.currentUser?.uid)
            Glide.with(this).load(imageRef).into(binding.imageProfile)
        }


    }

    private fun initAuthButton() {
        if(prefManager.getBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED)) {
            binding.btnAuth.text = "로그인"
            binding.btnAuth.setOnClickListener {
                prefManager.putBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED, false)
                startActivity(Intent(context, AuthHomeActivity::class.java))
                activity?.finish()
            }
        } else {
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
        _binding = null
    }
}