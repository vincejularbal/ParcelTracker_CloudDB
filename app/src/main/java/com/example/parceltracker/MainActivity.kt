package com.example.parceltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var parcelRecyclerView: RecyclerView
    private lateinit var adapter: ParcelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parcelRecyclerView = findViewById(R.id.parcelRecyclerView)

        adapter = ParcelAdapter(ParcelRepository.parcels,
            onViewHistory = { trackingNumber ->
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("trackingNumber", trackingNumber)
                startActivity(intent)
            },
            onUpdate = { trackingNumber ->
                val intent = Intent(this, EditParcelActivity::class.java)
                intent.putExtra("trackingNumber", trackingNumber)
                startActivity(intent)
            },
            onDelete = { trackingNumber ->
                val removed = ParcelRepository.deleteParcel(trackingNumber)
                if (removed) {
                    Toast.makeText(this, "Parcel deleted", Toast.LENGTH_SHORT).show()
                    refreshList()
                } else {
                    Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
                }
            }
        )

        parcelRecyclerView.layoutManager = LinearLayoutManager(this)
        parcelRecyclerView.adapter = adapter

        findViewById<Button>(R.id.btnAddParcel).setOnClickListener {
            startActivity(Intent(this, AddParcelActivity::class.java))
        }

        findViewById<Button>(R.id.btnScanQR).setOnClickListener {
            startActivity(Intent(this, QRScannerActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        adapter.updateList(ParcelRepository.parcels)
    }
}
