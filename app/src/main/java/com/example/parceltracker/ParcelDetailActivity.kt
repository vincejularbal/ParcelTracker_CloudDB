package com.example.parceltracker

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ParcelDetailActivity : AppCompatActivity() {

    private lateinit var tvTrackingNumber: TextView
    private lateinit var tvRecipient: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvLocation: TextView
    private lateinit var imgQrCode: ImageView

    private lateinit var btnUpdate: Button
    private lateinit var btnHistory: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parcel_detail)

        tvTrackingNumber = findViewById(R.id.tvTrackingNumber)
        tvRecipient = findViewById(R.id.tvRecipient)
        tvAddress = findViewById(R.id.tvAddress)
        tvStatus = findViewById(R.id.tvStatus)
        tvLocation = findViewById(R.id.tvLocation)
        imgQrCode = findViewById(R.id.imgQrCode)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnHistory = findViewById(R.id.btnViewHistory)
        btnDelete = findViewById(R.id.btnDelete)

        val trackingNumber = intent.getStringExtra("trackingNumber")
        val parcel = ParcelRepository.findParcelByTrackingNumber(trackingNumber ?: "")

        if (parcel == null) {
            Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bind data
        tvTrackingNumber.text = "Tracking #: ${parcel.trackingNumber}"
        tvRecipient.text = "Recipient: ${parcel.recipientName}"
        tvAddress.text = "Address: ${parcel.address}"
        val latest = parcel.history.lastOrNull()
        tvStatus.text = "Status: ${latest?.status ?: "N/A"}"
        tvLocation.text = "Location: ${latest?.location ?: "N/A"}"
        imgQrCode.setImageBitmap(parcel.qrBitmap)

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java).apply {
                putExtra("trackingNumber", parcel.trackingNumber)
            })
        }

        btnUpdate.setOnClickListener {
            startActivity(Intent(this, EditParcelActivity::class.java).apply {
                putExtra("trackingNumber", parcel.trackingNumber)
            })
        }

        btnDelete.setOnClickListener {
            val removed = ParcelRepository.deleteParcel(parcel.trackingNumber)
            if (removed) {
                Toast.makeText(this, "Parcel deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
