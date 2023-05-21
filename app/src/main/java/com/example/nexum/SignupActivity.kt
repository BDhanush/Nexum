package com.example.nexum
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupButton:Button=findViewById(R.id.signupButton)
        val loginButton:Button=findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            finish()
            Intent(this,LoginActivity::class.java).also{
                startActivity(it)
            }
        }
        signupButton.setOnClickListener {
            signup()
        }
    }
    private fun signup()
    {
        val email:EditText=findViewById(R.id.emailInput)
        val password:EditText=findViewById(R.id.passwordInput)
        val confirmPassword:EditText=findViewById(R.id.confirmPasswordInput)
        val firstName:EditText=findViewById(R.id.firstnameInput)
        val lastName:EditText=findViewById(R.id.lastnameInput)
        val p: Pattern = Pattern.compile("^[a-z]+([2][0-9])(ucse|uari|ucam|umee|uece|ueee)[0-9][0-9][0-9]@mahindrauniversity.edu.in\$")
        val m: Matcher = p.matcher(email.text.toString())
        val emailCheck: Boolean = m.matches()


        auth = Firebase.auth
        database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        if (!emailCheck) {
            Toast.makeText(
                this, "Only provide Mahindra email.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password.text.toString() != confirmPassword.text.toString()) {
            Toast.makeText(
                this, "Passwords do not match.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener(this) {

            if (it.isSuccessful) {
                Toast.makeText(
                    this, "Authentication successful.",
                    Toast.LENGTH_SHORT
                ).show()

                writeNewUser(auth.currentUser!!.uid, firstName.text.toString() , lastName.text.toString(), email = email.text.toString())

                finish()

            } else {
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun writeNewUser(uid:String,firstName:String,lastName:String,email:String) {
        val user = User(firstName,lastName,email);
        database.child("users").child(uid).setValue(user)
    }
}

