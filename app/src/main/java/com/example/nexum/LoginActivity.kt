package com.example.nexum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.nexum.signup.SignupActivity1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton:Button=findViewById(R.id.loginButton)
        val signupButton:Button=findViewById(R.id.signupButton)
        val forgotPasswordLink: TextView = findViewById(R.id.forgotPasswordLink)

        signupButton.setOnClickListener{
            Intent(this, SignupActivity1::class.java).also{
                startActivity(it)
            }
        }
        loginButton.setOnClickListener{
            auth = Firebase.auth
            val email: EditText =findViewById(R.id.usernameInput)
            val password: EditText =findViewById(R.id.passwordInput)
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            this, "signInWithEmail:success",
                            Toast.LENGTH_SHORT
                        ).show()
                        val user = auth.currentUser
                        finish()
                        Intent(this,MainActivity::class.java).also {
                            startActivity(it)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this, "signInWithEmail:failure ${task.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        forgotPasswordLink.setOnClickListener{
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }



    }

    public override fun onStart() {

        super.onStart()
        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Intent(this,MainActivity::class.java).also{
                finish()
                startActivity(it)
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("moveTaskToBack(true)"))
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
