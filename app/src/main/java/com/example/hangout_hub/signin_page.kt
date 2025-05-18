package com.example.hangout_hub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class signin_page : AppCompatActivity() {
    lateinit var logemail: EditText
    lateinit var logpassword: EditText
    lateinit var btnlogin: Button
    lateinit var btnregister: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_page)
        logemail =findViewById(R.id.edtlogemail)
        logpassword = findViewById(R.id.edtlogpassword)
        btnlogin = findViewById(R.id.btn2login)
        btnregister =findViewById(R.id.btn2register)

        auth = FirebaseAuth.getInstance()

        btnregister.setOnClickListener {
            var gotoregister = Intent(this, register_page::class.java)
            startActivity(gotoregister)
            startActivity(Intent(this,register_page::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)


        }

        btnlogin.setOnClickListener {

            var email = logemail.text.toString().trim()
            var password = logpassword.text.toString().trim()

            if(email.isEmpty() or password.isEmpty()){
                Toast.makeText(this, "Cannot Submit Empty Field", Toast.LENGTH_SHORT).show()
            }else{

                auth.signInWithEmailAndPassword(email ,password).addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()

                        var gotomain = Intent(this, MainActivity::class.java)
                        startActivity(gotomain)
                        finish()
                    }else{

                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()

                    }

                }
            }

        }





    }
}