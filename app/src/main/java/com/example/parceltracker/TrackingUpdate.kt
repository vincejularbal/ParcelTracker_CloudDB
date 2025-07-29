package com.example.parceltracker

import java.time.LocalDateTime

data class TrackingUpdate(
    val timestamp: LocalDateTime,
    val location: String,
    val status: String
)
