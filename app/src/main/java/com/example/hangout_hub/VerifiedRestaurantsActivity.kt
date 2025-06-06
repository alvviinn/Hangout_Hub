package com.example.hangout_hub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangout_hub.databinding.ActivityVerifiedRestaurantsBinding
import com.google.firebase.database.*
import java.util.*


class VerifiedRestaurantsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifiedRestaurantsBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var database: DatabaseReference
    private val restaurantList = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifiedRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restaurantAdapter = RestaurantAdapter(restaurantList) { restaurant ->
            val intent = Intent(this, RestaurantBookingActivity::class.java)
            intent.putExtra("restaurantId", restaurant.id)
            startActivity(intent)
        }

        binding.recyclerViewRestaurants.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRestaurants.adapter = restaurantAdapter

        loadVerifiedRestaurants()
    }

    private fun loadVerifiedRestaurants() {
        binding.progressBar.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().getReference("Restaurants")

        database.orderByChild("isVerified").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurantList.clear()
                for (restaurantSnap in snapshot.children) {
                    val restaurant = restaurantSnap.getValue(Restaurant::class.java)
                    restaurant?.let {
                        it.id = restaurantSnap.key ?: ""
                        restaurantList.add(it)
                    }
                }
                restaurantAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VerifiedRestaurantsActivity, "Failed to load restaurants", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}