package com.nowontourist.tourist.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

object FirebaseUtil {
    const val COLLECTION_USERS = "users"
    const val KEY_EMAIL = "email"
    const val KEY_NAME = "name"
}

fun FirebaseStorage.uploadUserProfile(uid: String?, uri: Uri): UploadTask {
    val imageRef = getReference("users/${uid?:"Unknown"}")
    return imageRef.putFile(uri)
}
