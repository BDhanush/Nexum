package com.example.nexum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.SharedImage
import com.example.nexum.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var user: User
    val SELECT_IMAGE=200
    var selectedImageUri: Uri?=null
    lateinit var profilePicture:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePicture=view.findViewById(R.id.profilePicture)
        val firstName:TextView=view.findViewById(R.id.firstNameInput)
        val lastName:TextView=view.findViewById(R.id.lastNameInput)
        val submitButton:Button=view.findViewById(R.id.submit)
        val addImage:Button=view.findViewById(R.id.editImage)

        val auth=Firebase.auth
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users/${auth.currentUser!!.uid}")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val userMap = dataSnapshot.value as Map<String, Any?>
                user = userFromMap(userMap)

                user.uid = auth.currentUser!!.uid

                firstName.text = user.firstName
                lastName.text = user.lastName
                Glide.with(view)
                    .load(user.profilePicture)
                    .into(profilePicture)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        addImage.setOnClickListener {
            addimage()
        }
        submitButton.setOnClickListener {
            if(user.firstName!=firstName.text.toString() || user.lastName!=lastName.text.toString() || selectedImageUri!=null) {
                user.firstName=firstName.text.toString()
                user.lastName=lastName.text.toString()
                updateUser()
            }else
                Toast.makeText(this.context,"No Updates", Toast.LENGTH_SHORT).show()

        }

    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun updateUser()
    {

        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        if(selectedImageUri!=null)
        {
            val storageRef= Firebase.storage

            val ref = storageRef.reference.child("images/${user.uid}/profilePicture")
            // Compress the image before uploading
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
            val compressedBitmap = compressBitmap(bitmap, 500) // Adjust the desired max image size here
            val baos = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 0, baos) // Adjust the desired compression quality here
            val data = baos.toByteArray()

            ref.putBytes(data)
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
                    user.profilePicture=downloadUri.toString()
                    val updates=user.toMap()

                    database.child("users").child(user.uid!!).updateChildren(updates)
                    Toast.makeText(this.context,"Profile Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            val updates=user.toMap()

            database.child("users").child(user.uid!!).updateChildren(updates)
            Toast.makeText(this.context,"Profile Updated", Toast.LENGTH_SHORT).show()
        }


    }

    private fun compressBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val aspectRatio: Float = width.toFloat() / height.toFloat()

        if (width > maxSize || height > maxSize) {
            if (aspectRatio > 1) {
                width = maxSize
                height = (width / aspectRatio).toInt()
            } else {
                height = maxSize
                width = (height * aspectRatio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
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
            profilePicture.setImageURI(selectedImageUri)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}