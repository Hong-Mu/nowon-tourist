package com.nowontourist.tourist.ui.home

import java.util.Date

data class Event(
    val title: String,
    val description: String,
    val imageUrl: String,
    val startDate: Date,
    val endDate: Date,
    val location: String,
)