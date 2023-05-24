package com.example.nexum

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.GridView
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
import com.example.nexum.adapter.GridViewAdapter
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityAddOpportunityBinding
import com.example.nexum.databinding.EventDetailsBinding

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: EventDetailsBinding
    private lateinit var gridView: GridView

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
        gridView = findViewById(R.id.gridView)

        val imageUrls = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b4/Lionel-Messi-Argentina-2022-FIFA-World-Cup_%28cropped%29.jpg",
            "https://dailypost.ng/wp-content/uploads/2023/05/Messi.jpg","https://cdn.britannica.com/35/238335-050-2CB2EB8A/Lionel-Messi-Argentina-Netherlands-World-Cup-Qatar-2022.jpg",
            "https://dailypost.ng/wp-content/uploads/2022/11/3cabcc81540d0491.jpg"
        )
        val adapter = GridViewAdapter(imageUrls)
        gridView.adapter = adapter

//        gridView.setOnItemClickListener { parent, view, position, id ->
//            val imageUrl = imageUrls[position]
//            openImage(imageUrl)
//        }


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
