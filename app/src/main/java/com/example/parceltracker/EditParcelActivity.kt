package com.example.parceltracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

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

        val trackingNumber = intent.getStringExtra("trackingNumber")
        parcel = ParcelRepository.findParcelByTrackingNumber(trackingNumber ?: "")

        if (parcel == null) {
            Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnAddUpdate.setOnClickListener {
            val location = etLocation.text.toString().trim()
            val status = spinnerStatus.selectedItem.toString()

            if (location.isEmpty() || status.isEmpty()) {
                Toast.makeText(this, "Fill both location and status", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val update = TrackingUpdate(
                timestamp = LocalDateTime.now(),
                location = location,
                status = status
            )

            parcel?.history?.add(update)

            Toast.makeText(this, "Update added!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
