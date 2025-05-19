package com.example.hangout_hub

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddHangout : AppCompatActivity() {

    lateinit var addimgview: ImageView
    lateinit var addtxtview: TextView
    lateinit var edtaddname: EditText
    lateinit var edtaddcontacts: EditText
    lateinit var edtaddlocation: EditText
    lateinit var edtadddescription: EditText
    lateinit var btnchooseimage: Button
    lateinit var btnuploaddata: Button
    lateinit var btnback2main: ImageButton

    var fileUri: Uri? = null
    lateinit var storageReference: StorageReference
    lateinit var progressDialog: ProgressDialog

    companion object {
        const val PICK_IMAGE_REQUEST = 22
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hangout)

        // Initialize Views
        addimgview = findViewById(R.id.add_imageview)
        addtxtview = findViewById(R.id.Add_textview)
        edtaddname = findViewById(R.id.edtlocationname)
        edtaddcontacts = findViewById(R.id.edtlocationcontact)
        edtaddlocation = findViewById(R.id.edtlocation)
        edtadddescription = findViewById(R.id.edtlocationdescription)
        btnchooseimage = findViewById(R.id.btnlocationimage)
        btnuploaddata = findViewById(R.id.btnlocationupload)
        btnback2main = findViewById(R.id.btnimgback)

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.setMessage("Please wait...")

        // Choose Image
        btnchooseimage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose image to upload"), PICK_IMAGE_REQUEST)
        }

        // Upload Data
        btnuploaddata.setOnClickListener {
            uploadDataWithImage()
        }

        // Go back to main
        btnback2main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            addimgview.setImageURI(fileUri)
        }
    }

    private fun uploadDataWithImage() {
        val name = edtaddname.text.toString().trim()
        val contacts = edtaddcontacts.text.toString().trim()
        val location = edtaddlocation.text.toString().trim()
        val description = edtadddescription.text.toString().trim()
        val time_id = System.currentTimeMillis().toString()

        if (name.isEmpty() || contacts.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (fileUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.show()

        val imageRef = storageReference.child("Names/${UUID.randomUUID()}")

        imageRef.putFile(fileUri!!)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    val hangout = Hangout(name, contacts, location, description, imageUrl, time_id)

                    val dbRef = FirebaseDatabase.getInstance().reference.child("Names/$time_id")
                    dbRef.setValue(hangout).addOnCompleteListener { task ->
                        progressDialog.dismiss()
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Hangout uploaded successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, View_Activity::class.java))
                        } else {
                            Toast.makeText(this, "Failed to upload data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}
