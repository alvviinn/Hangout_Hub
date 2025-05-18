package com.example.hangout_hub

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddHangout : AppCompatActivity() {
    lateinit var addimgview:ImageView
    lateinit var addtxtview:TextView
    lateinit var edtaddname:EditText
    lateinit var edtaddcontacts:EditText
    lateinit var edtaddlocation:EditText
    lateinit var edtadddescription:EditText
    lateinit var btnchooseimage:Button
    lateinit var btnuploaddata:Button
    lateinit var btnback2main:Button
    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hangout)

        addimgview = findViewById(R.id.add_imageview)
        addtxtview= findViewById(R.id.Add_textview)
        edtaddname = findViewById(R.id.edtlocationname)
        edtaddcontacts = findViewById(R.id.edtlocationcontact)
        edtaddlocation = findViewById(R.id.edtlocation)
        edtadddescription = findViewById(R.id.edtlocationdescription)
        btnchooseimage = findViewById(R.id.btnlocationimage)
        btnuploaddata = findViewById(R.id.btnlocationupload)



        btnchooseimage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent,"Choose image to upload"),22
            )



        }




        btnuploaddata.setOnClickListener {

            uploadImage()


            var name = edtaddname.text.toString().trim()
            var contacts= edtaddcontacts.text.toString().trim()
            var location = edtaddlocation.text.toString().trim()
            var description = edtadddescription.text.toString().trim()

            var time_id = System.currentTimeMillis().toString()

            //progress bar
            var progress = ProgressDialog(this)
            progress.setTitle("Saving Data")
            progress.setMessage("Please Wait")

            if (name.isEmpty() or contacts.isEmpty() or location.isEmpty() or description.isEmpty()){
                Toast.makeText(this, "Cannot Submit Empty Field", Toast.LENGTH_SHORT).show()}
            else{

                var my_child = FirebaseDatabase.getInstance().reference.child("Names/"+time_id)
                var user_data = Hangout(name,contacts,location,description,time_id)

                progress.show()
                //save data
                my_child.setValue(user_data).addOnCompleteListener{

                    if(it.isSuccessful){
                        Toast.makeText(this, "Hangout uploaded Successfully", Toast.LENGTH_SHORT).show()

                        var gotoview = Intent(this,View_Activity::class.java)
                        startActivity(gotoview)


                    }else{
                        Toast.makeText(this, "Failed to Upload Hangout", Toast.LENGTH_SHORT).show()
                    }
                }





            }

        }




    }
    // on below line creating a function to upload our image.
    fun uploadImage() {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(fileUri!!).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}