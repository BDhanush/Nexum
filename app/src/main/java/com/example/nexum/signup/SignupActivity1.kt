package com.example.nexum.signup
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.LoginActivity

import com.example.nexum.R
import com.example.nexum.databinding.ActivitySignup1Binding
import com.example.nexum.databinding.ActivitySignupBinding
import com.example.nexum.signup.SignupActivity2
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignupActivity1 : AppCompatActivity() {
    // Inside SignupActivity1 class

    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var binding: ActivitySignup1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val nextButton: Button = findViewById(R.id.nextButton)
//        val loginButton: Button = findViewById(R.id.loginButton)

        // Retrieve firstname and lastname values from TextInputEditTexts


//        loginButton.setOnClickListener {
//            finish()
//            Intent(this, LoginActivity::class.java).also {
//                startActivity(it)
//            }
//        }

        resetFields()
        nextButton.setOnClickListener {
            if(checkFields()) {
                next()
            }
        }
    }

    private fun next() {
        val firstnameInput: TextInputEditText = findViewById(R.id.firstnameInput)
        val lastnameInput: TextInputEditText = findViewById(R.id.lastnameInput)
        firstname = firstnameInput.text.toString()
        lastname = lastnameInput.text.toString()
        // Start the next activity and pass firstname and lastname as extras
        val intent = Intent(this, SignupActivity2::class.java)

        intent.putExtra("firstname", firstname.trim())
        intent.putExtra("lastname", lastname.trim())
        startActivity(intent)
    }

    private fun checkFields():Boolean
    {
        var check:Boolean=true;
        if (binding.firstnameInput.toString().trim().isEmpty()) {
            binding.firstnameLayout.error = "This field is required"
            check = false
        }
        if (binding.lastnameInput.toString().trim().isEmpty()) {
            binding.lastnameLayout.error = "This field is required"
            check = false
        }
        // after all validation return true.
        return check
    }

    private fun resetFields() {
        binding.firstnameInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable) {
                if (binding.firstnameInput.toString().trim().isEmpty()) {
                    binding.firstnameLayout.error = "This field is required"
                }
                else {
                    binding.firstnameLayout.error = null
                }
            }
        })
        binding.lastnameInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable) {
                if (binding.lastnameInput.toString().trim().isEmpty()) {
                    binding.lastnameLayout.error = "This field is required"
                }
                else {
                    binding.lastnameLayout.error = null
                }
            }
        })
    }
}