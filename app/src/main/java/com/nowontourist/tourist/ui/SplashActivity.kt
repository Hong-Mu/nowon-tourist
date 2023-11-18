package com.nowontourist.tourist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nowontourist.tourist.databinding.ActivitySplashBinding
import com.nowontourist.tourist.ui.auth.AuthHomeActivity
import com.nowontourist.tourist.ui.auth.InputProfileActivity
import com.nowontourist.tourist.util.FirebaseUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        db = Firebase.firestore

        // 3초 후 실행
        lifecycleScope.launch {
            delay(2000)
            checkAuth()
        }
    }

    private fun checkAuth() {
        if(auth.currentUser != null) {
            val ref = db.collection(FirebaseUtil.COLLECTION_USERS).document(auth.currentUser!!.uid)
            ref.get().addOnSuccessListener { snapShot ->
                if(snapShot.exists()) {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                } else {
                    startActivity(Intent(this, InputProfileActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                // TODO 예외 처리
            }

        } else {
            startActivity(Intent(this@SplashActivity, AuthHomeActivity::class.java))
            finish()
        }
    }
}