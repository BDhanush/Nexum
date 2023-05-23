package com.example.nexum

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nexum.databinding.ActivityMainBinding
import com.example.nexum.databinding.OpportunityDetailsBinding
import com.example.nexum.firebasefunctions.oppoFromMap
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.Opportunity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.sql.Date
import java.text.SimpleDateFormat

class OpportunityDetailsActivity : AppCompatActivity() {
    private lateinit var binding: OpportunityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = OpportunityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val key=intent.getStringExtra("key")
        var opportunity:Opportunity?=null
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("opportunities")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val map=dataSnapshot.child(key!!).value as Map<String,Any?>
                opportunity= oppoFromMap(map)
                opportunity!!.key=key
                updateData(opportunity!!)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })



    }
    fun updateData(opportunity: Opportunity)
    {
        binding.applyButton.setOnClickListener {
            val browse = Intent(Intent.ACTION_VIEW, Uri.parse(opportunity!!.link))
            startActivity(browse)
        }
        binding.link.text = "link: "+opportunity.link
        binding.title.text=opportunity.title
        binding.description.text=opportunity.description
        binding.datePosted.text= SimpleDateFormat("dd/MM/yy hh:mm a").format(Date(opportunity.datePosted))
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val userMap=dataSnapshot.child(opportunity.uid!!).value as Map<String,Any?>
                val user = userFromMap(userMap)
                binding.username.text = user.firstName + " " + user.lastName
                if(user.profilePicture!=null)
                    Picasso.get().load(user.profilePicture).into(binding.profilePicture);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}