package com.nowontourist.tourist.util

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage

object FirebaseUtil {
    const val COLLECTION_USERS = "users"
    const val COLLECTION_GALLERY = "gallery"

    const val KEY_EMAIL = "email"
    const val KEY_NAME = "name"


}

fun FirebaseStorage.getUserProfileRef(uid: String?): StorageReference {
    return getReference("users/${uid?:"Unknown"}")
}

fun FirebaseStorage.uploadUserProfile(uid: String?, uri: Uri): UploadTask {
    val imageRef = getUserProfileRef(uid)
    return imageRef.putFile(uri)
}

fun FirebaseStorage.getGalleryRef(docId: String?): StorageReference {
    return getReference("gallery/${docId}")
}

fun FirebaseStorage.uploadGallery(docId: String, uri: Uri): UploadTask {
    val imageRef = getGalleryRef(docId)
    return imageRef.putFile(uri)
}

val firebaseAuth get() = Firebase.auth
val firebaseDatabase get() = Firebase.firestore
val firebaseStorage get() = Firebase.storage
