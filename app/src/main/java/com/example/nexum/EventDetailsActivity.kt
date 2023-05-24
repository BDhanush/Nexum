package com.example.nexum

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.example.nexum.databinding.EventDetailsBinding
import com.example.nexum.firebasefunctions.eventFromMap
import com.example.nexum.model.Event
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: EventDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = EventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val key=intent.getStringExtra("key")
        var event: Event?=null
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("events")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val map=dataSnapshot.child(key!!).value as Map<String,Any?>
                event= eventFromMap(map)
                event!!.key=key
                updateData(event!!)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })


    }
    fun updateData(event: Event){
        binding.collapsingToolbar.title=event.title
        binding.description.text=event.description
        binding.date.text=event.date
        binding.time.text=event.time
        binding.location.text="Venue: "+event.venue

        if(event.previewImage!=null)
        {
            Picasso.get().load(event.previewImage).into(binding.imageView, object : Callback {
                override fun onSuccess() {
                    binding.imageView.buildDrawingCache()
                    val bitmap: Bitmap = binding.imageView.getDrawingCache()

                    Palette.Builder(bitmap).generate { it?.let { palette ->
                        val dominantColor = palette.getDominantColor(Color.LTGRAY)
                        binding.toolbar.setBackgroundColor(dominantColor)
                    }
                    }
                }
                override fun onError(e: Exception?) {
                }
            })
        }

    }

}
