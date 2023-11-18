package com.nowontourist.tourist.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nowontourist.tourist.databinding.ActivitySignUpBinding
import com.nowontourist.tourist.ui.MainActivity

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        // 회원가입 버튼
        binding.btnSignUp.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()
            signUp(email, password, confirmPassword)
        }

        // 로그인 텍스트
        binding.textSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        }
    }



    private fun signUp(email: String, password: String, confirmPassword: String) {
        if(!isValid(email, password, confirmPassword)) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, InputProfileActivity::class.java))
                finish()
            }.addOnFailureListener {
                Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun isValid(email: String, password: String, confirmPassword: String): Boolean {
        if(email.isBlank()) {
            Snackbar.make(binding.root, "이메일을 입력하여 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(password.isBlank()) {
            Snackbar.make(binding.root, "비밀번호를 입력하여 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(confirmPassword.isBlank()) {
            Snackbar.make(binding.root, "비밀번호 확인을 입력하여 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(password != confirmPassword) {
            Snackbar.make(binding.root, "비밀번호가 일치하지 않습니다", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}