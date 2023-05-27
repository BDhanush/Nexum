package com.example.nexum.signup
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.LoginActivity

import com.example.nexum.R
import com.example.nexum.signup.SignupActivity2
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference



class SignupActivity1 : AppCompatActivity() {
    // Inside SignupActivity1 class

    private lateinit var firstname: String
    private lateinit var lastname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)

        val nextButton: Button = findViewById(R.id.nextButton)
//        val loginButton: Button = findViewById(R.id.loginButton)

        // Retrieve firstname and lastname values from TextInputEditTexts


//        loginButton.setOnClickListener {
//            finish()
//            Intent(this, LoginActivity::class.java).also {
//                startActivity(it)
//            }
//        }

        nextButton.setOnClickListener {
            next()
        }
    }

    private fun next() {
        val firstnameInput: TextInputEditText = findViewById(R.id.firstnameInput)
        val lastnameInput: TextInputEditText = findViewById(R.id.lastnameInput)
        firstname = firstnameInput.text.toString()
        lastname = lastnameInput.text.toString()
        // Start the next activity and pass firstname and lastname as extras
        val intent = Intent(this, SignupActivity2::class.java)

        intent.putExtra("firstname", firstname)
        intent.putExtra("lastname", lastname)
        startActivity(intent)
    }


}