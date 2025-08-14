package com.example.parceltracker

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ParcelRepository {

    private val db = Firebase.firestore
    private val parcelsCol = db.collection("parcels")

    // Local cache (backed by Firestore snapshots)
    val parcels: MutableList<Parcel> = mutableListOf()

    // Simple observer pattern so Activities can refresh when data changes
    private val observers = mutableListOf<(List<Parcel>) -> Unit>()
    private var registration: ListenerRegistration? = null

    fun addObserver(observer: (List<Parcel>) -> Unit) {
        observers += observer
        // push current cache immediately
        observer(parcels)
    }

    fun removeObserver(observer: (List<Parcel>) -> Unit) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        val copy = parcels.toList()
        observers.forEach { it(copy) }
    }

    /** Start listening to Firestore (idempotent) */
    fun startListening() {
        if (registration != null) return
        registration = parcelsCol.addSnapshotListener { snap, err ->
            if (err != null || snap == null) return@addSnapshotListener
            parcels.clear()
            for (doc in snap.documents) {
                val p = doc.toObject(Parcel::class.java)
                if (p != null) parcels += p
            }
            notifyObservers()
        }
    }

    fun stopListening() {
        registration?.remove()
        registration = null
    }

    fun findParcelByTrackingNumber(trackingNumber: String): Parcel? {
        return parcels.find { it.trackingNumber == trackingNumber }
    }

    fun getParcelOnce(trackingNumber: String, onResult: (Parcel?) -> Unit) {
        parcelsCol.document(trackingNumber).get()
            .addOnSuccessListener { snap -> onResult(snap.toObject(Parcel::class.java)) }
            .addOnFailureListener { onResult(null) }
    }

    fun addParcel(parcel: Parcel, onDone: (Boolean, String?) -> Unit) {
        // Use trackingNumber as document id for easy lookups
        parcelsCol.document(parcel.trackingNumber)
            .set(parcel)
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }

    fun addUpdate(trackingNumber: String, update: TrackingUpdate, onDone: (Boolean, String?) -> Unit) {
        parcelsCol.document(trackingNumber)
            .update("history", FieldValue.arrayUnion(update))
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }

    fun deleteParcel(trackingNumber: String, onDone: (Boolean, String?) -> Unit) {
        parcelsCol.document(trackingNumber)
            .delete()
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }
}
