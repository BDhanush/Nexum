package com.example.nexum.adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.*
import android.widget.SeekBar.*
import androidx.core.content.ContextCompat.startActivity
import com.example.nexum.R
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.AddOpportunityActivity
import com.example.nexum.EventDetailsActivity
import com.example.nexum.model.Event
import com.google.firebase.database.*

class EventItemAdapter(private val context: Context, val dataset:MutableList<Event>): RecyclerView.Adapter<EventItemAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val addButton:Button = view.findViewById(R.id.addButton)
        init{
            addButton.setOnClickListener {
                val intent=Intent(view.context, EventDetailsActivity::class.java)
                view.context.startActivity(intent)
            }
        }
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
        holder.addButton.setOnClickListener {
            val intent=Intent(context, EventDetailsActivity::class.java)
            context.startActivity(intent)
        }

    }
    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}