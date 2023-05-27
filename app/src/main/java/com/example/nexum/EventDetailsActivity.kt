package com.example.nexum

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.widget.NestedScrollView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.nexum.adapter.EventItemAdapter
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
import com.example.nexum.databinding.EventDetailsBinding
import com.example.nexum.firebasefunctions.imageFromMap
import com.example.nexum.model.SharedImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: EventDetailsBinding
    var dataset:MutableList<SharedImage> = mutableListOf()
    lateinit var adapter:GridViewAdapter
    var key:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        key=intent.getStringExtra("key")
        readImages()
        super.onCreate(savedInstanceState)
        binding = EventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var event: Event?=null
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("events")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val map=dataSnapshot.child(key!!).value as Map<String,Any?>
                event= eventFromMap(map)
                event!!.key=key
                updateData(event!!,view)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        binding.nestedScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                val dy=scrollY-oldScrollY
                val dx=scrollX-oldScrollX

                // if the recycler view is scrolled
                // above hide the addButton
                if (dy > 10 && binding.addButton.isShown) {
                    binding.addButton.hide()
                }

                // if the recycler view is
                // scrolled above show the addButton
                if (dy < -10 && !binding.addButton.isShown) {
                    binding.addButton.show()
                }

                // of the recycler view is at the first
                // item always show the addButton
                if (!v.canScrollVertically(-1)) {
                    binding.addButton.show()
                }
            }
        })
        binding.addButton.setOnClickListener{
            Intent(this,AddEventPhotosActivity::class.java).also{
                it.putExtra("key", key)
                startActivity(it)
            }
        }
        binding.toggleButtons.addOnButtonCheckedListener { group, checkedId, isChecked ->
            filtered(binding.toggleButtons.checkedButtonIds, event!!.uid!!)
        }


    }
    private fun readImages()
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("eventImages/$key")
        val auth= Firebase.auth
        database.addValueEventListener(object :ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                for(snapshot in dataSnapshot.children) {
                    val eventMap=snapshot.value as Map<String,Any?>
                    val image=imageFromMap(eventMap)
                    image.eventKey=key
                    image.imageKey=snapshot.key
                    dataset.add(image)
                }

                adapter = GridViewAdapter(dataset)
                binding.gridView.adapter=adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

    }

    private fun filtered(checked:List<Int>,creatorId:String)
    {
        val filtereredList:MutableList<SharedImage> = mutableListOf()
        if(checked.isEmpty())
        {
            adapter = GridViewAdapter(dataset)
            binding.gridView.adapter=adapter
        }
        val auth=Firebase.auth
        val curUser=auth.currentUser!!.uid
        for(image in dataset){
            for(i in checked){
                if(image.uid==creatorId && i==binding.organizer.id){
                    filtereredList.add(image)
                    break
                }else if(curUser == image.uid && i==binding.you.id){
                    filtereredList.add(image)
                    break
                }else if(curUser != image.uid && creatorId!=curUser && i==binding.users.id){
                    filtereredList.add(image)
                    break
                }
            }
        }

        adapter = GridViewAdapter(filtereredList)
        binding.gridView.adapter=adapter
    }
    fun updateData(event: Event,view:ConstraintLayout){
        binding.collapsingToolbar.title=event.title
        binding.description.text=event.description
        binding.date.text=event.date
        binding.time.text=event.time
        binding.location.text="Venue: "+event.venue

        if(event.previewImage!=null)
        {
            Glide.with(this).load(event.previewImage!!.toUri()).into(object : CustomViewTarget<ConstraintLayout, Drawable>(
                view
            ) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // error handling
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    // clear all resources
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.imageView.setImageDrawable(resource)
                    binding.imageView.buildDrawingCache()
                    val bitmap: Bitmap = binding.imageView.getDrawingCache()
                    Palette.Builder(bitmap).generate { it?.let { palette ->
                        val dominantColor = palette.getDominantColor(Color.LTGRAY)

                        binding.collapsingToolbar.setBackgroundColor(dominantColor)
                        binding.collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(dominantColor));
                        binding.collapsingToolbar.setContentScrimColor(palette.getMutedColor(dominantColor));

                    }
                    }
                }
            })
        }


    }

}
