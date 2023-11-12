package com.nowontourist.tourist.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nowontourist.tourist.databinding.ActivityInputProfileBinding
import com.nowontourist.tourist.ui.MainActivity

class InputProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInputProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnUploadProfile.setOnClickListener {

        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}