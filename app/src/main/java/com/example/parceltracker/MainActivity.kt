package com.example.parceltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var parcelRecyclerView: RecyclerView
    private lateinit var adapter: ParcelAdapter

    // Activity Result API launcher for FirebaseUI auth (sign-in + sign-up)
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> onSignInResult(res) }

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
                    Toast.makeText(
                        this,
                        if (ok) "Parcel deleted" else "Delete failed: $msg",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        parcelRecyclerView.layoutManager = LinearLayoutManager(this)
        parcelRecyclerView.adapter = adapter

        findViewById<Button>(R.id.btnAddParcel).setOnClickListener {
            if (ensureSignedIn()) startActivity(Intent(this, AddParcelActivity::class.java))
        }
        findViewById<Button>(R.id.btnScanQR).setOnClickListener {
            if (ensureSignedIn()) startActivity(Intent(this, QRScannerActivity::class.java))
        }
        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
                    launchSignIn() // Prompt sign-in again
                }
        }


    }

    override fun onStart() {
        super.onStart()
        ensureSignedIn()
        ParcelRepository.startListening()
        ParcelRepository.addObserver(repoObserver)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onStop() {
        super.onStop()
        ParcelRepository.removeObserver(repoObserver)
    }

    private fun refreshList() = adapter.updateList(ParcelRepository.parcels)

    /** Returns true if a user is signed in; otherwise launches FirebaseUI and returns false. */
    private fun ensureSignedIn(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) true else {
            launchSignIn()
            false
        }
    }

    private fun launchSignIn() {
        // Email provider; sign‑up appears after entering a brand-new email and tapping NEXT
        val emailProvider = AuthUI.IdpConfig.EmailBuilder()
            // .setAllowNewAccounts(true) // optional; only available on some versions
            // .setRequireName(true)      // optional
            .build()

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(arrayListOf(emailProvider))
            // .setIsSmartLockEnabled(false) // remove: not available in your version
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            refreshList()
            return
        }

        // Show the real error (if any)
        val err = result.idpResponse?.error
        val msg = err?.localizedMessage ?: "Sign-up/sign-in canceled"
        android.util.Log.e("AuthUI", "Auth error", err)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

        // If you require auth to use the app:
        finish()
    }
}
