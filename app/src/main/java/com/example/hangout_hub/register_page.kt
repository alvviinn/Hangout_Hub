package com.example.hangout_hub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class register_page : AppCompatActivity() {
    lateinit var Rname: EditText
    lateinit var Remail: EditText
    lateinit var Rpassword: EditText
    lateinit var Rlogin: Button
    lateinit var Rregister: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)


        Rname = findViewById(R.id.edtregname)
        Remail= findViewById(R.id.edtregemail)
        Rpassword = findViewById(R.id.edtregpassword)
        Rlogin = findViewById(R.id.btnloglogin)
        Rregister = findViewById(R.id.btnredreg)

        auth = FirebaseAuth.getInstance()

        Rlogin.setOnClickListener {
            var gotologin= Intent(this, signin_page::class.java)
            startActivity(gotologin)
            onBackPressed()

        }
        Rregister.setOnClickListener {

            var name = Rname.text.toString().trim()
            var email = Remail.text.toString().trim()
            var password = Rpassword.text.toString().trim()

            if (name.isEmpty() or email.isEmpty() or password.isEmpty()){
                Toast.makeText(this, "Cannot Submit Empty Field", Toast.LENGTH_SHORT).show()

            }else{

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){

                    if (it.isSuccessful){
                        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()

                        var gotoLogin_activityogin = Intent(this,signin_page::class.java)
                        startActivity(gotoLogin_activityogin)
                        finish()



                    }else{
                        Toast.makeText(this, "Failed to Create Account", Toast.LENGTH_SHORT).show()
                    }
                }


            }

        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }
}


