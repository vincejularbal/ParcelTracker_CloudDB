package com.example.parceltracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParcelAdapter(
    private var parcelList: List<Parcel>,
    private val onViewHistory: (String) -> Unit,
    private val onUpdate: (String) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder>() {

    inner class ParcelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrackingNumber: TextView = itemView.findViewById(R.id.tvTrackingNumber)
        val tvRecipient: TextView = itemView.findViewById(R.id.tvRecipient)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnViewHistory: Button = itemView.findViewById(R.id.btnViewHistory)
        val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_parcel, parent, false)
        return ParcelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParcelViewHolder, position: Int) {
        val parcel = parcelList[position]
        val lastUpdate = parcel.history.lastOrNull()
        val qrView = holder.itemView.findViewById<ImageView>(R.id.imgQrCode)

        holder.tvTrackingNumber.text = "Tracking #: ${parcel.trackingNumber}"
        holder.tvRecipient.text = "Recipient: ${parcel.recipientName}"
        holder.tvAddress.text = "Address: ${parcel.address}"
        holder.tvStatus.text = "Status: ${lastUpdate?.status ?: "N/A"}"
        holder.tvLocation.text = "Location: ${lastUpdate?.location ?: "N/A"}"

        qrView.setImageBitmap(parcel.qrBitmap)
        holder.btnViewHistory.setOnClickListener { onViewHistory(parcel.trackingNumber) }
        holder.btnUpdate.setOnClickListener { onUpdate(parcel.trackingNumber) }
        holder.btnDelete.setOnClickListener { onDelete(parcel.trackingNumber) }
    }

    override fun getItemCount(): Int = parcelList.size

    fun updateList(newList: List<Parcel>) {
        parcelList = newList
        notifyDataSetChanged()
    }
}
