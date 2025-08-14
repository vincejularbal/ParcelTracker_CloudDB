package com.example.parceltracker

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var tvHeader: TextView
    private lateinit var tvHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tvHeader = findViewById(R.id.tvHeader)
        tvHistory = findViewById(R.id.tvHistory)

        val trackingNumber = intent.getStringExtra("trackingNumber") ?: ""

        val parcel = ParcelRepository.findParcelByTrackingNumber(trackingNumber)
        if (parcel == null) {
            Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvHeader.text = """
            📦 Tracking #: ${parcel.trackingNumber}
            👤 Recipient: ${parcel.recipientName}
            📍 Address: ${parcel.address}
        """.trimIndent()

        if (parcel.history.isEmpty()) {
            tvHistory.text = "No tracking updates yet."
        } else {
            val updates = parcel.history.joinToString("\n\n") {
                "🕒 ${it.timestamp.toDate()}\n📍 ${it.location}\n🚚 ${it.status}"
            }
            tvHistory.text = updates
        }
    }
}
