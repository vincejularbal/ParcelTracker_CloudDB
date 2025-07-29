package com.example.parceltracker

import android.graphics.Bitmap

data class Parcel(
    val trackingNumber: String,
    var recipientName: String,
    var address: String,
    val history: MutableList<TrackingUpdate> = mutableListOf(),
    @Transient var qrBitmap: Bitmap? = null
)
