package com.nowontourist.tourist.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.nowontourist.tourist.databinding.ActivityInputProfileBinding
import com.nowontourist.tourist.ui.MainActivity
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.uploadUserProfile

class InputProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInputProfileBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                uploadProfile(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage

        binding.btnUploadProfile.setOnClickListener {
            imageLauncher.launch(Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            })
        }

        binding.btnSignUp.setOnClickListener {
            auth.currentUser?.let {
                val name = binding.inputName.text.toString()
                saveUserInfo(it.uid, it.email, name)
            }
        }
    }

    // TODO: 로딩 프로그래스바 추가
    private fun uploadProfile(uri: Uri?) {
        uri?.let {
            storage.uploadUserProfile(auth.currentUser?.uid, it)
                .addOnSuccessListener {  _ ->
                    Glide.with(this).load(it).into(binding.imageProfile)
                }.addOnFailureListener { e ->
                    Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveUserInfo(uid: String, email: String?, name: String) {
        val user = hashMapOf(
            FirebaseUtil.KEY_EMAIL to email,
            FirebaseUtil.KEY_NAME to name,
        )

        db.collection(FirebaseUtil.COLLECTION_USERS)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }.addOnFailureListener { e ->
                Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }
    }
}