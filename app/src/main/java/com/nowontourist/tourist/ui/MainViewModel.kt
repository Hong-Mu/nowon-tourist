package com.nowontourist.tourist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.getUserProfileRef

class MainViewModel: ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _stamps = MutableLiveData<Map<String, Boolean>>()
    val stamps: LiveData<Map<String, Boolean>> = _stamps

    init {
        loadUserData()
    }

    private fun loadUserData() {
        firebaseAuth.currentUser?.let {
            firebaseDatabase.collection(FirebaseUtil.COLLECTION_USERS)
                .document(it.uid)
                .addSnapshotListener { snapshot, error ->
                    if(snapshot != null && snapshot.exists()) {
                        val user = snapshot.toObject(User::class.java)
                        if(user != null) {
                            _userName.value = user.name
                            _stamps.value = user.stamps?: hashMapOf()
                        }
                    }

                }
        }
    }
}