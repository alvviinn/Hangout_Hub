package com.example.hiddengems

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterViewFlipper
import android.widget.Button
import android.widget.Toast
import com.example.hangout_hub.ImageFlipperAdapter
import com.example.hangout_hub.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    lateinit var btnaddhangout: Button
    private lateinit var flipper: AdapterViewFlipper
    private val imageUrls = mutableListOf<String>()
    private val dbRef = FirebaseDatabase.getInstance().getReference("slideshow_images")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnaddhangout = findViewById(R.id.btn_add_hangout)
        flipper = findViewById(R.id.Adapter_flipper_home)



        btnaddhangout.setOnClickListener {
            val gotoAddhangout = Intent(this, AddHangout::class.java)
            startActivity(gotoAddhangout)


        }

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
                Toast.makeText(this@MainActivity, "Failed to load images", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
