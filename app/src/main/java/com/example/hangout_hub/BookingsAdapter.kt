package com.example.hangout_hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BookingsAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtDate: TextView = itemView.findViewById(R.id.txtBookingDate)
        private val txtTime: TextView = itemView.findViewById(R.id.txtBookingTime)
        private val txtPeople: TextView = itemView.findViewById(R.id.txtPeopleCount)

        fun bind(booking: Booking) {
            txtDate.text = "Date: ${booking.date}"
            txtTime.text = "Time: ${booking.time}"
            txtPeople.text = "People: ${booking.numberOfPeople}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size
}
