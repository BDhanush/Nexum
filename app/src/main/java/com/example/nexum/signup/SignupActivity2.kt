package com.example.nexum.signup
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.LoginActivity

import com.example.nexum.R
import com.google.android.material.textfield.TextInputEditText
import com.example.nexum.signup.SignupActivity3


class SignupActivity2 : AppCompatActivity() {

    private lateinit var emailName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        val nextButton: Button = findViewById(R.id.nextButton)


        nextButton.setOnClickListener {
            val firstname = intent.getStringExtra("firstname").toString()
            val lastname = intent.getStringExtra("lastname").toString()


            // Retrieve firstname and lastname values from TextInputEditTexts
            val emailnameInput: TextInputEditText = findViewById(R.id.emailInput)
            emailName = emailnameInput.text.toString()

            val intent = Intent(this, SignupActivity3::class.java)
            intent.putExtra("firstname", firstname)
            intent.putExtra("lastname", lastname)
            intent.putExtra("emailName", emailName)

            startActivity(intent)
        }
    }

}