package com.example.parceltracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddParcelActivity : AppCompatActivity() {

    private lateinit var etTrackingNumber: EditText
    private lateinit var etRecipientName: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSaveParcel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parcel)

        etTrackingNumber = findViewById(R.id.etTrackingNumber)
        etRecipientName = findViewById(R.id.etRecipientName)
        etAddress = findViewById(R.id.etAddress)
        btnSaveParcel = findViewById(R.id.btnSaveParcel)

        btnSaveParcel.setOnClickListener {
            val tn = etTrackingNumber.text.toString().trim()
            val name = etRecipientName.text.toString().trim()
            val addr = etAddress.text.toString().trim()

            if (tn.isEmpty() || name.isEmpty() || addr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newParcel = Parcel(tn, name, addr)
            try {
                val encoder = com.journeyapps.barcodescanner.BarcodeEncoder()
                val bitmap = encoder.encodeBitmap(tn, com.google.zxing.BarcodeFormat.QR_CODE, 300, 300)
                newParcel.qrBitmap = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ParcelRepository.parcels.add(newParcel)

            Toast.makeText(this, "Parcel added!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
