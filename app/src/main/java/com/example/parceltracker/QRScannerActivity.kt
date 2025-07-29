package com.example.parceltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator

class QRScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan Parcel QR Code")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val scannedTrackingNumber = result.contents

                val matched = ParcelRepository.findParcelByTrackingNumber(scannedTrackingNumber)
                if (matched != null) {
                    // You can open History or Edit screen
                    val intent = Intent(this, ParcelDetailActivity::class.java)
                    intent.putExtra("trackingNumber", scannedTrackingNumber)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Parcel not found", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
