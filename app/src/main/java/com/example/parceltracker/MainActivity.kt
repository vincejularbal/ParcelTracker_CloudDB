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

    private val repoObserver: (List<Parcel>) -> Unit = { list ->
        adapter.updateList(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parcelRecyclerView = findViewById(R.id.parcelRecyclerView)
        adapter = ParcelAdapter(
            ParcelRepository.parcels,
            onViewHistory = { trackingNumber ->
                startActivity(Intent(this, HistoryActivity::class.java).apply {
                    putExtra("trackingNumber", trackingNumber)
                })
            },
            onUpdate = { trackingNumber ->
                startActivity(Intent(this, EditParcelActivity::class.java).apply {
                    putExtra("trackingNumber", trackingNumber)
                })
            },
            onDelete = { trackingNumber ->
                ParcelRepository.deleteParcel(trackingNumber) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Parcel deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Delete failed: $msg", Toast.LENGTH_SHORT).show()
                    }
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

    override fun onStart() {
        super.onStart()
        ParcelRepository.startListening()
        ParcelRepository.addObserver(repoObserver)
    }

    override fun onStop() {
        super.onStop()
        ParcelRepository.removeObserver(repoObserver)
        // (optional) keep listening globally by NOT calling stopListening()
    }
}
