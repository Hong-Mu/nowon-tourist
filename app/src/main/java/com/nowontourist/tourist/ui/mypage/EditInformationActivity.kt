package com.nowontourist.tourist.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.ActivityEditInformationBinding

class EditInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun saveProfileChanges() {
        val newName = binding.editTextName.text.toString()
        val newPhoneNumber = binding.editTextPhoneNumber.text.toString()
        val newNickname = binding.editTextNickname.text.toString()

        //ToDo: 수정된 정보 저장 홍무야 이거 디비에 연결 어케해 help me

        finish()
    }
}