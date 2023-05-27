
package com.example.nexum
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var resetPasswordButton: Button
    private lateinit var loginBackTextView: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

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
                    Toast.makeText(applicationContext, "Reset Link Sent", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Error occurred while sending password reset email
                    // You can show an error message or perform any other action here
                    Toast.makeText(applicationContext, "Email does not exist!", Toast.LENGTH_SHORT).show()

                }
            }
    }
}
