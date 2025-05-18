package com.example.hangout_hub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class View_Activity : AppCompatActivity() {
    lateinit var mylistview:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mylistview = findViewById(R.id.my_list_view)

        var hangout: ArrayList<Hangout> = ArrayList()
        var myadapter = CustomAdapter(applicationContext,hangout)

        var my_db = FirebaseDatabase.getInstance().reference.child("Names")
        my_db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get data and display in array
                hangout.clear()
                for (snap in snapshot.children){
                    var person = snap.getValue(Hangout::class.java)
                    hangout.add(person!!)
                }
                myadapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to Display Data", Toast.LENGTH_SHORT).show()

            }

        })
        mylistview.adapter = myadapter

    }
}