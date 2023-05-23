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
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityAddOpportunityBinding
import com.example.nexum.databinding.ActivityAddSharedBinding
import com.example.nexum.model.Event
import com.example.nexum.model.SharedFile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.net.URI

class AddSharedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSharedBinding
    val SELECT_FILE=200
    var selectedFileUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSharedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.selectFile.setOnClickListener {
            addFile()
        }
        binding.submit.setOnClickListener {
            val auth = Firebase.auth

            if(checkFields())
            {
                val sharedFile=SharedFile(
                    auth.currentUser!!.uid,
                    binding.renameInput.text.toString() + binding.fileName.text.toString().dropWhile { it!='.' },
                    null
                )
                uploadFile(sharedFile)
            }

        }
    }

    private fun uploadFile(sharedFile: SharedFile)
    {
        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val key = database.child("files").push().key
        if(selectedFileUri!=null)
        {
            val storageRef= Firebase.storage

            val ref = storageRef.reference.child("files/$key")
            val uploadTask = ref.putFile(selectedFileUri!!)

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
                    sharedFile.fileURL=downloadUri.toString()
                    database.child("files").child(key!!).setValue(sharedFile)
                    Toast.makeText(this,"File added", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


    }
    private fun checkFields():Boolean
    {
        var check:Boolean=true;

        if (selectedFileUri==null) {
            binding.selectFile.error = "Select file"
            check = false
        }
        // after all validation return true.
        return check

    }
    private fun addFile() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
    }

    @SuppressLint("Range")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==SELECT_FILE)
        {
            selectedFileUri=data!!.data
            binding.renameInput.setText(selectedFileUri.toString())
            val cursor = contentResolver.query(data.data!!, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val fileName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                binding.fileName.setText("Selected File: $fileName")
                binding.renameInput.setText(fileName.dropLastWhile { it!='.' }.dropLast(1))
            }

            binding.renameLayout.visibility= View.VISIBLE
            binding.fileName.visibility= View.VISIBLE

        }
    }
}