package com.nowontourist.tourist.ui.gallery

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryItem(
    val id: String ="",
    val title: String ="",
    val content: String = "",
    val author: String = "",
    val likes: List<String>? = null,
) : Parcelable