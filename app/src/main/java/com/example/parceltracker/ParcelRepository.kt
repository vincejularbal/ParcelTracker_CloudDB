package com.example.parceltracker

object ParcelRepository {
    val parcels = mutableListOf<Parcel>()

    fun findParcelByTrackingNumber(trackingNumber: String): Parcel? {
        return parcels.find { it.trackingNumber == trackingNumber }
    }

    fun deleteParcel(trackingNumber: String): Boolean {
        return parcels.removeIf { it.trackingNumber == trackingNumber }
    }
}
