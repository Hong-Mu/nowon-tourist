package com.nowontourist.tourist.ui

data class User(
    val name: String = "",
    val stamps: Map<String, Boolean>? = null,
)