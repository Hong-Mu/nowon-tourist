package com.nowontourist.tourist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.ActivityMainBinding
import com.nowontourist.tourist.ui.auth.InputProfileActivity
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.SharedPreferencesManager
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var prefManger: SharedPreferencesManager
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        checkUserProfile()
    }

    private fun checkUserProfile() {
        if(prefManger.getBoolean(SharedPreferencesManager.KEY_AUTH_SKIPPED)) {
            return
        }

        firebaseAuth.currentUser?.let {
            val ref = firebaseDatabase.collection(FirebaseUtil.COLLECTION_USERS).document(it.uid)
            ref.get().addOnSuccessListener { snapShot ->
                if(!snapShot.exists()) {
                    startActivity(Intent(this, InputProfileActivity::class.java))
                }
            }.addOnFailureListener {
                // TODO 예외 처리
            }
        }
    }
}