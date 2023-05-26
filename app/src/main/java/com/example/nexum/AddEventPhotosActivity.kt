package com.example.nexum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import com.example.nexum.databinding.ActivityAddEventPhotosBinding
import com.example.nexum.model.SharedImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddEventPhotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventPhotosBinding
    val SELECT_IMAGE=200
    var selectedImageUri: Uri?=null
    lateinit var eventKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventPhotosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        eventKey= intent.getStringExtra("key").toString()

        binding.selectImage.setOnClickListener {
            addimage()
        }

        binding.seeImage.setOnClickListener {
            if(selectedImageUri!=null) {
                val intent = Intent(baseContext, DisplayImageActivity::class.java)
                intent.putExtra("imageUrl", selectedImageUri.toString())
                startActivity(intent)
            }
        }


        binding.submit.setOnClickListener {
            val auth = Firebase.auth

            if(checkFields())
            {
                val sharedImage= SharedImage(
                    auth.currentUser!!.uid
                )
                uploadImage(sharedImage)
            }

        }
    }

    private fun uploadImage(sharedImage: SharedImage)
    {

        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val key = database.child("eventImages").child(eventKey).push().key
        if(selectedImageUri!=null)
        {
            val storageRef= Firebase.storage

            val ref = storageRef.reference.child("images/$eventKey/$key")
            val uploadTask = ref.putFile(selectedImageUri!!)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    sharedImage.imageURL=downloadUri.toString()
                    database.child("eventImages").child(eventKey).child(key!!).setValue(sharedImage)
                    Toast.makeText(this,"Image added", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


    }
    private fun checkFields():Boolean
    {
        var check:Boolean=true;

        if (selectedImageUri==null) {
            binding.selectImage.error = "Select image"
            check = false
        }
        // after all validation return true.
        return check

    }
    private fun addimage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    @SuppressLint("Range")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==SELECT_IMAGE)
        {
            selectedImageUri=data!!.data
            binding.seeImage.visibility=View.VISIBLE
            val cursor = contentResolver.query(data.data!!, null, null, null, null)
//            if (cursor != null && cursor.moveToFirst()) {
//                val imageName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                binding.imageName.setText("Selected Image: $imageName")
//            }

//            binding.imageName.visibility= View.VISIBLE

        }
    }
}