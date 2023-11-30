package com.nowontourist.tourist.ui.home

import java.util.Date

data class Event(
    val title: String = "",
    val startDate: Date? = null,
    val endDate: Date? = null,
    val location: String = "",
    val url: String = "",
)