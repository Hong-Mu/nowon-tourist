package com.nowontourist.tourist.ui.gallery

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.nowontourist.tourist.databinding.ActivityEditGalleryBinding
import com.nowontourist.tourist.util.FirebaseUtil
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.uploadGallery
import com.nowontourist.tourist.util.uploadUserProfile

class EditGalleryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditGalleryBinding.inflate(layoutInflater) }

    private var imageUploaded = false
    private var imageUri: Uri? = null
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUri = result.data?.data
                uploadImage(imageUri)
            }
        }

    private lateinit var docRef: DocumentReference // Temporary document
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        docRef = firebaseDatabase.collection(FirebaseUtil.COLLECTION_GALLERY).document()

        binding.imageMain.setOnClickListener {
            imageLauncher.launch(Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            })
        }

        binding.inputContent.addTextChangedListener {
            updateUI()
        }

        binding.btnSave.setOnClickListener {
            save()
        }
    }

    private fun uploadImage(uri: Uri?) {
        uri?.let {
            binding.progressBar.visibility = View.VISIBLE
            firebaseStorage.uploadGallery(docRef.id, it)
                .addOnSuccessListener {  _ ->
                    Glide.with(this).load(it).into(binding.imageMain)
                    binding.textUpload.text = ""
                    imageUploaded = true
                }.addOnFailureListener { e ->
                    Snackbar.make(binding.root, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
                }.addOnCompleteListener {
                    binding.progressBar.visibility = View.INVISIBLE
                    updateUI()
                }
        }
    }

    private fun updateUI() {
        binding.btnSave.isEnabled = imageUploaded && binding.inputContent.text.toString().isNotBlank()
    }

    private fun save() {
        val item = GalleryItem(docRef.id, "", binding.inputContent.text.toString(), firebaseAuth.currentUser!!.uid)
        docRef.set(item)
            .addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Snackbar.make(binding.root, "잠시 후 다시 시도하여 주세요!", Snackbar.LENGTH_SHORT).show()
            }
    }
}