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

    private lateinit var emailname: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        val nextButton: Button = findViewById(R.id.nextButton)
        val firstname = intent.getStringExtra("firstname")
        val lastname = intent.getStringExtra("lastname")


        // Retrieve firstname and lastname values from TextInputEditTexts
        val emailnameInput: TextInputEditText = findViewById(R.id.emailInput)
        emailname = emailnameInput.text.toString()

        nextButton.setOnClickListener {
            val intent = Intent(this, SignupActivity3::class.java)
            intent.putExtra("firstname", firstname)
            intent.putExtra("lastname", lastname)
            intent.putExtra("emailname", emailname)

            startActivity(intent)
        }
    }

}