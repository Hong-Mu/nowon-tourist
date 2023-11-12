package com.nowontourist.tourist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nowontourist.tourist.databinding.ActivitySplashBinding
import com.nowontourist.tourist.ui.auth.AuthHomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        // 3초 후 실행
        lifecycleScope.launch {
            delay(2000)
            checkAuth()
        }
    }

    /**
     * 로그인 상태 확인하여 이동
     */
    private fun checkAuth() {
        if(auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        } else {
            startActivity(Intent(this@SplashActivity, AuthHomeActivity::class.java))
            finish()
        }
    }
}