package com.example.kotlintracker.registerlogin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintracker.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {

            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login","Attempt Login with email/pw:$email/***")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    //else if sucessful
                    Log.d("Main","Sucessfully Logged IN")
                }
                .addOnFailureListener {
                    Log.d("Main","Failed to Login: ${it.message}")
                }
        }
        back_to_register_textview.setOnClickListener {
            finish()
        }





    }


}
