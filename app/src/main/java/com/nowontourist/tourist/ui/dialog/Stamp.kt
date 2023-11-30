package com.nowontourist.tourist.ui.dialog

data class Stamp(
    val id: Int,
    val description: String,
    val title: String,
    val imageUrl: String,
    var isStamped: Boolean,
)