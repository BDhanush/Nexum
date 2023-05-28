
package com.example.nexum
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.databinding.ActivityForgotPasswordBinding
import com.example.nexum.databinding.ActivitySignup2Binding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var resetPasswordButton: Button
    private lateinit var loginBackTextView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        emailInputLayout = findViewById(R.id.inputEmail)
        emailEditText = findViewById(R.id.editEmail)

        resetPasswordButton = findViewById(R.id.resetPassword)
        resetPasswordButton.setOnClickListener {
            resetPassword()
        }

        loginBackTextView = findViewById(R.id.loginBack)
        loginBackTextView.setOnClickListener {
            finish()
        }
    }

    private fun resetPassword() {
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailInputLayout.error = "Please enter your email"
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Reset Link Sent.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Error occurred while sending password reset email
                    // You can show an error message or perform any other action here
                    Toast.makeText(applicationContext, "There is no account associated with this email address.", Toast.LENGTH_LONG).show()
                    emailEditText.requestFocus()
                    emailEditText.setText("")

                }
            }
    }
}
