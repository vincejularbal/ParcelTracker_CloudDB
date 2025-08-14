package com.example.parceltracker

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude

data class Parcel(
    var trackingNumber: String = "",
    var recipientName: String = "",
    var address: String = "",
    var history: MutableList<TrackingUpdate> = mutableListOf(),
    @get:Exclude @set:Exclude @Transient var qrBitmap: Bitmap? = null
)
