package com.example.parceltracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp

class EditParcelActivity : AppCompatActivity() {

    private lateinit var etLocation: EditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnAddUpdate: Button
    private var parcel: Parcel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_parcel)

        etLocation = findViewById(R.id.etLocation)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnAddUpdate = findViewById(R.id.btnAddUpdate)

        val statuses = listOf("In Transit", "Delivered", "Lost", "Unknown Address")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        val trackingNumber = intent.getStringExtra("trackingNumber") ?: ""

        parcel = ParcelRepository.findParcelByTrackingNumber(trackingNumber)
        if (parcel == null) {
            // Try fetching once from backend (in case local cache isn’t ready)
            ParcelRepository.getParcelOnce(trackingNumber) { fetched ->
                parcel = fetched
                if (parcel == null) {
                    Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        btnAddUpdate.setOnClickListener {
            val location = etLocation.text.toString().trim()
            val status = spinnerStatus.selectedItem.toString()

            if (location.isEmpty()) {
                Toast.makeText(this, "Enter a location", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val update = TrackingUpdate(
                timestamp = Timestamp.now(),
                location = location,
                status = status
            )

            ParcelRepository.addUpdate(trackingNumber, update) { ok, msg ->
                if (ok) {
                    Toast.makeText(this, "Update added!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add update: $msg", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
