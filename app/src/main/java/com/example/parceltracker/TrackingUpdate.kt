package com.example.parceltracker

import com.google.firebase.Timestamp

data class TrackingUpdate(
    var timestamp: Timestamp = Timestamp.now(),
    var location: String = "",
    var status: String = ""
)
