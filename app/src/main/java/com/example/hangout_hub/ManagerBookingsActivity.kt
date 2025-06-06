package com.example.hangout_hub

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangout_hub.databinding.ActivityManagerBookingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ManagerBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagerBookingsBinding
    private lateinit var bookingsAdapter: BookingsAdapter
    private lateinit var bookingsRef: DatabaseReference
    private lateinit var restaurantsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val bookingsList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings")
        restaurantsRef = FirebaseDatabase.getInstance().getReference("Restaurants")

        bookingsAdapter = BookingsAdapter(bookingsList)
        binding.recyclerViewManagerBookings.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewManagerBookings.adapter = bookingsAdapter

        loadManagerRestaurantId()
    }

    private fun loadManagerRestaurantId() {
        val currentUserId = auth.currentUser?.uid ?: return
        restaurantsRef.orderByChild("ownerId").equalTo(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val restaurantId = snapshot.children.first().key ?: return
                        loadBookings(restaurantId)
                    } else {
                        Toast.makeText(this@ManagerBookingsActivity, "No restaurant found for this manager.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ManagerBookingsActivity, "Error loading restaurant info", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadBookings(restaurantId: String) {
        bookingsRef.orderByChild("restaurantId").equalTo(restaurantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookingsList.clear()
                    for (bookingSnap in snapshot.children) {
                        val booking = bookingSnap.getValue(Booking::class.java)
                        booking?.let { bookingsList.add(it) }
                    }
                    bookingsAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ManagerBookingsActivity, "Failed to load bookings", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
    }
}
