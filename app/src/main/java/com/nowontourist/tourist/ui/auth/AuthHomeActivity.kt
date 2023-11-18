package com.nowontourist.tourist.ui.auth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.ActivityAuthHomeBinding
import com.nowontourist.tourist.ui.MainActivity

class AuthHomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAuthHomeBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val oneTapClientLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    signInWithCredential(credential)
                } catch (exception: ApiException) {
                    Snackbar.make(binding.root, exception.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        db = Firebase.firestore
        initGoogleAuth()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }

        binding.btnGoogleLogin.setOnClickListener {
            oneTapClient
                .beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        oneTapClientLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                    } catch (e: IntentSender.SendIntentException) {
                        Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener(this) { e ->
                    Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    private fun initGoogleAuth() {

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.google_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun signInWithCredential(credential: SignInCredential) {
        val idToken = credential.googleIdToken
        if(idToken != null) {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener {
                    startActivity(Intent(this, InputProfileActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }
        }
    }
}