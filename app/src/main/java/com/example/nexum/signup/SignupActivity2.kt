package com.example.nexum.signup
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.LoginActivity

import com.example.nexum.R
import com.example.nexum.databinding.ActivitySignup2Binding
import com.google.android.material.textfield.TextInputEditText
import com.example.nexum.signup.SignupActivity3
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignupActivity2 : AppCompatActivity() {

    private lateinit var emailName: String
    private lateinit var binding: ActivitySignup2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val nextButton: Button = findViewById(R.id.nextButton)

        resetFields()
        nextButton.setOnClickListener {
            if(checkFields()) {
                next()
            }
        }
    }

    private fun next() {
        val firstname = intent.getStringExtra("firstname").toString()
        val lastname = intent.getStringExtra("lastname").toString()


        // Retrieve firstname and lastname values from TextInputEditTexts
        val emailInput: TextInputEditText = findViewById(R.id.emailInput)
        emailName = emailInput.text.toString()

        val intent = Intent(this, SignupActivity3::class.java)
        intent.putExtra("firstname", firstname)
        intent.putExtra("lastname", lastname)
        intent.putExtra("emailName", emailName)

        startActivity(intent)
    }

    private fun checkFields():Boolean
    {
        var check:Boolean=true;
        val p: Pattern =
            Pattern.compile("^[a-z]+([2][0-9])(ucse|uari|ucam|umee|uece|ueee)[0-9][0-9][0-9]@mahindrauniversity.edu.in\$")
        val m: Matcher = p.matcher(binding.emailInput.text.toString())
        val emailCheck: Boolean = m.matches()
        if (binding.emailInput.text.toString().isEmpty()) {
            binding.emailLayout.error = "This field is required"
            check = false
        } else if (!emailCheck) {
            binding.emailLayout.error = "Only provide Mahindra email"
            check = false
        }
        // after all validation return true.
        return check
    }

    private fun resetFields() {
        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable) {
                val p: Pattern =
                    Pattern.compile("^[a-z]+([2][0-9])(ucse|uari|ucam|umee|uece|ueee)[0-9][0-9][0-9]@mahindrauniversity.edu.in\$")
                val m: Matcher = p.matcher(binding.emailInput.text.toString())
                val emailCheck: Boolean = m.matches()
                if (binding.emailInput.text.toString().isEmpty()) {
                    binding.emailLayout.error = "This field is required"
                } else if (!emailCheck) {
                    binding.emailLayout.error = "Only provide Mahindra email"
                }
                else {
                    binding.emailLayout.error = null
                }
            }
        })
    }

}