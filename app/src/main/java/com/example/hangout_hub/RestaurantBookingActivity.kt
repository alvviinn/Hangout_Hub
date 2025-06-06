// RestaurantBookingActivity.kt
package com.example.hangout_hub

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class RestaurantBookingActivity : AppCompatActivity() {

    private lateinit var txtName: TextView
    private lateinit var txtLocation: TextView
    private lateinit var txtContact: TextView
    private lateinit var imgRestaurant: ImageView
    private lateinit var btnBook: Button
    private lateinit var edtPeople: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var bookingsRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private var restaurantId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_booking)

        // Initialize Views
        txtName = findViewById(R.id.txtDetailName)
        txtLocation = findViewById(R.id.txtDetailLocation)
        txtContact = findViewById(R.id.txtDetailContact)
        imgRestaurant = findViewById(R.id.imgDetailRestaurant)
        btnBook = findViewById(R.id.btnMakeBooking)
        edtPeople = findViewById(R.id.edtPeopleCount)
        edtDate = findViewById(R.id.edtBookingDate)
        edtTime = findViewById(R.id.edtBookingTime)

        // Firebase setup
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Restaurants")
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings")

        // Get restaurant ID
        restaurantId = intent.getStringExtra("restaurantId")

        if (restaurantId != null) {
            loadRestaurantDetails(restaurantId!!)
        }

        // Date & Time pickers
        edtDate.setOnClickListener { showDatePicker() }
        edtTime.setOnClickListener { showTimePicker() }

        // Submit booking
        btnBook.setOnClickListener { submitBooking() }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            edtDate.setText("$day/${month + 1}/$year")
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            edtTime.setText(String.format("%02d:%02d", hour, minute))
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    private fun submitBooking() {
        val peopleStr = edtPeople.text.toString().trim()
        val date = edtDate.text.toString().trim()
        val time = edtTime.text.toString().trim()

        if (peopleStr.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val booking = Booking(
            restaurantId = restaurantId ?: "",
            userId = auth.currentUser?.uid ?: "unknown",
            numberOfPeople = peopleStr.toInt(),
            date = date,
            time = time
        )

        val key = bookingsRef.push().key ?: return
        bookingsRef.child(key).setValue(booking).addOnSuccessListener {
            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRestaurantDetails(id: String) {
        database.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val restaurant = snapshot.getValue(Restaurant::class.java)
                restaurant?.let {
                    txtName.text = it.name
                    txtLocation.text = it.location
                    txtContact.text = it.contact
                    Glide.with(this@RestaurantBookingActivity)
                        .load(it.imageUrl)
                        .into(imgRestaurant)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@RestaurantBookingActivity,
                    "Failed to load details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
