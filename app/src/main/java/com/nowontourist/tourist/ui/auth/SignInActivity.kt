package com.nowontourist.tourist.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nowontourist.tourist.databinding.ActivitySignInBinding
import com.nowontourist.tourist.ui.MainActivity

class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            signIn(email, password)
        }

        // 회원가입 텍스트
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        if(!isValid(email, password)) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }.addOnFailureListener {
                Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun isValid(email: String, password: String): Boolean {
        if(email.isBlank()) {
            Snackbar.make(binding.root, "이메일을 입력하여 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(password.isBlank()) {
            Snackbar.make(binding.root, "비밀번호를 입력하여 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
