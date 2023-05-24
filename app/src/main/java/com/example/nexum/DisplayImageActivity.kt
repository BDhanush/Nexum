
package com.example.nexum

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.nexum.adapter.GridViewAdapter
import com.example.nexum.firebasefunctions.imageFromMap
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class DisplayImageActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    var imageKey:String?=null
    var eventKey:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        imageKey=intent.getStringExtra("imageKey")
        eventKey=intent.getStringExtra("eventKey")

        val imageUrl = intent.getStringExtra("imageUrl")
        val owner=intent.getStringExtra("owner")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        imageView = findViewById(R.id.imageView)

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl.toUri())
                .into(imageView)
        }
        val auth=Firebase.auth
        val deleteButton:Button=findViewById(R.id.deleteButton)

        if(owner==auth.currentUser!!.uid)
        {
            deleteButton.visibility= View.VISIBLE
        }else{
            deleteButton.visibility= View.GONE
        }
        deleteButton.setOnClickListener {
            deleteCurImage()
        }
    }
    private fun deleteCurImage()
    {
        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("eventImages/$eventKey/$imageKey")
        database.removeValue()

        val storageRef = Firebase.storage.reference
        val desertRef = storageRef.child("images/$eventKey/$imageKey")

        desertRef.delete().addOnSuccessListener {
            Toast.makeText(baseContext,"Image Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }

    }
}
