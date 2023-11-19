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
import com.nowontourist.tourist.util.SharedPreferencesManager
import com.nowontourist.tourist.util.firebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var prefManger: SharedPreferencesManager
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 3초 후 실행
        lifecycleScope.launch {
            delay(2000)
            checkAuth()
        }
    }

    private fun checkAuth() {
        if(prefManger.getBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED)) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            return
        }

        if(firebaseAuth.currentUser == null) {
            startActivity(Intent(this, AuthHomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }
}