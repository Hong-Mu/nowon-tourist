package com.nowontourist.tourist.ui.gallery

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.nowontourist.tourist.R
import com.nowontourist.tourist.databinding.ActivityGalleryDetailBinding
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase
import com.nowontourist.tourist.util.firebaseStorage
import com.nowontourist.tourist.util.getGalleryRef
import com.nowontourist.tourist.util.getParcelableExtra

class GalleryDetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_DATA = "key_data"
    }

    private val binding by lazy { ActivityGalleryDetailBinding.inflate(layoutInflater) }
    private var galleryItem: GalleryItem? = null

    private lateinit var docRef: DocumentReference

    private lateinit var orangeState: ColorStateList
    private lateinit var grayState: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        orangeState = ColorStateList.valueOf(resources.getColor(R.color.orange))
        grayState = ColorStateList.valueOf(resources.getColor(R.color.gray))

        galleryItem = intent.getParcelableExtra(key = KEY_DATA)
        galleryItem?.let { item ->
            docRef = firebaseDatabase.document("gallery/${item.id}")
            update(item)
        }

        docRef.addSnapshotListener(this) { snapshot, error ->
            snapshot?.toObject(GalleryItem::class.java)?.let {
                update(it)
            }
        }
    }

    private fun update(item: GalleryItem) {
        Glide.with(binding.root)
            .load(firebaseStorage.getGalleryRef(item.id))
            .into(binding.imageMain)

        binding.textContent.text = item.content

        val likes = item.likes?.size?:0
        binding.textLikes.text = likes.toString()

        if(item.likes?.contains(firebaseAuth.currentUser?.uid?:"") == true) {
            binding.imageLikes.setImageResource(R.drawable.ic_heart_fill)
            binding.imageLikes.imageTintList = orangeState
            binding.textLikes.setTextColor(orangeState)
        } else {
            binding.imageLikes.setImageResource(R.drawable.ic_heart_blank)
            binding.imageLikes.imageTintList = grayState
            binding.textLikes.setTextColor(grayState)
        }

        binding.btnLike.setOnClickListener {
            if(firebaseAuth.currentUser != null) {
                if(item.likes?.contains(firebaseAuth.currentUser!!.uid) == true) {
                    docRef.update("likes", FieldValue.arrayRemove(firebaseAuth.currentUser!!.uid))
                } else {
                    docRef.update("likes", FieldValue.arrayUnion(firebaseAuth.currentUser!!.uid))
                }
            } else {
                Snackbar.make(binding.root, "로그인이 필요합니다!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}