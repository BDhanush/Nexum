package com.example.nexum.adapter

import android.content.Context
import android.view.*
import android.widget.*
import android.widget.SeekBar.*
import com.example.nexum.R
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.model.Event
import com.example.nexum.model.SharedFile
import com.google.firebase.database.*

class SharedItemAdapter(val dataset:MutableList<SharedFile>): RecyclerView.Adapter<SharedItemAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.shared_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
//        holder.username.text= getUsername(item.uid);
//        holder.profilePicture.setImageURI(Uri.parse(getProfilePicture(item.uid)));

    }
    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}