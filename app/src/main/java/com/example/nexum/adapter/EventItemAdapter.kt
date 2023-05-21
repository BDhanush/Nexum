package com.example.nexum.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import com.example.nexum.R
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.EventDetailsActivity
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.Event
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventItemAdapter(val dataset:MutableList<Event>): RecyclerView.Adapter<EventItemAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val previewImage:ImageView= view.findViewById(R.id.previewImage)
        val title:TextView=view.findViewById(R.id.title)
        val username:TextView=view.findViewById(R.id.username)
        val profilePicture:ShapeableImageView=view.findViewById(R.id.profilePicture)
        val interestButton:Button=view.findViewById(R.id.interestButton)
        val description:TextView=view.findViewById(R.id.description)
        val date:TextView=view.findViewById(R.id.date)
        val location:TextView=view.findViewById(R.id.location)
        val time:TextView=view.findViewById(R.id.time)











    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
//        holder.username.text= getUsername(item.uid);
//        holder.profilePicture.setImageURI(Uri.parse(getProfilePicture(item.uid)));

        holder.previewImage.setImageURI(item.previewImage?.toUri())
        holder.title.text=item.title
        holder.description.text=item.description
        holder.date.text=item.date
        holder.time.text=item.time
        holder.location.text= "Venue: "+item.venue

        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val userMap=dataSnapshot.child(item.uid!!).value as Map<String,Any?>
                val user = userFromMap(userMap)
                holder.username.text = user.firstName + " " + user.lastName
                if(user.profilePicture!=null)
                    holder.profilePicture.setImageURI(user.profilePicture!!.toUri())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}