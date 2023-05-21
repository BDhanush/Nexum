package com.example.nexum.adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import com.example.nexum.R
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.EventDetailsActivity
import com.example.nexum.model.Event
import com.google.android.material.imageview.ShapeableImageView

class EventItemAdapter(val dataset:MutableList<Event>): RecyclerView.Adapter<EventItemAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val previewImage:ImageView= view.findViewById(R.id.previewImage)
        val title:TextView=view.findViewById(R.id.title)
        val username:TextView=view.findViewById(R.id.username)
        val profilePicture:ShapeableImageView=view.findViewById(R.id.profilePicture)
        val button:Button=view.findViewById(R.id.button)
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
        holder.title.text = item.title
    }
    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}