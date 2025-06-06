package com.example.hangout_hub

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    // Drawer & navigation
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var profileIcon: ImageView

    // Firebase flipper
    private lateinit var flipper: AdapterViewFlipper
    private val imageUrls = mutableListOf<String>()
    private val dbRef = FirebaseDatabase.getInstance().getReference("slideshow_images")

    // UI Cards
    private lateinit var btnAddHangout: Button
    private lateinit var cardExplore: CardView
    private lateinit var cardRestaurants: CardView
    private lateinit var cardProfile: CardView // placeholder if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Drawer initialization
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        profileIcon = findViewById(R.id.profile_icon)

        profileIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Navigation item handling
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> showToast("Home clicked")

                R.id.nav_add_restaurant -> {
                    // Navigate to AddRestaurantActivity
                    val intent = Intent(this, AddRestaurantActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_logout -> {
                    showToast("Logging out...")
                    // Add logout logic here if needed
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // UI components
        flipper = findViewById(R.id.Adapter_flipper_home)
        btnAddHangout = findViewById(R.id.btn_add_hangout)
        cardExplore = findViewById(R.id.cardView3)
        cardRestaurants = findViewById(R.id.btnRestaurants)
        cardProfile = findViewById(R.id.cardView5)

        btnAddHangout.setOnClickListener {
            val intent = Intent(this, AddHangout::class.java)
            startActivity(intent)
        }

        cardExplore.setOnClickListener {
            val intent = Intent(this, View_Activity::class.java)
            startActivity(intent)
        }

        cardRestaurants.setOnClickListener {
            val intent = Intent(this, VerifiedRestaurantsActivity::class.java)
            startActivity(intent)
        }

        // Load images from Firebase into AdapterViewFlipper
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imageUrls.clear()
                for (child in snapshot.children) {
                    val url = child.getValue(String::class.java)
                    url?.let { imageUrls.add(it) }
                }

                val adapter = ImageFlipperAdapter(this@MainActivity, imageUrls)
                flipper.adapter = adapter
                flipper.flipInterval = 3000
                flipper.isAutoStart = true
                flipper.startFlipping()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to load images")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
