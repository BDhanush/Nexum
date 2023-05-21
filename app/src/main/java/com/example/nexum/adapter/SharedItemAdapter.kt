package com.example.nexum.adapter

import android.content.Context
import android.view.*
import android.widget.*
import android.widget.SeekBar.*
import com.example.nexum.R
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.model.Event
import com.example.nexum.model.SharedFile
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*

class SharedItemAdapter(private val context: Context, val dataset:MutableList<SharedFile>): RecyclerView.Adapter<SharedItemAdapter.ItemViewHolder>()
{
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val fileName:TextView=view.findViewById(R.id.fileName)
        val username:TextView=view.findViewById(R.id.username)
        val profilePicture: ShapeableImageView =view.findViewById(R.id.profilePicture)
        val downloadButton:Button=view.findViewById(R.id.downloadButton)
        val datePosted:TextView=view.findViewById(R.id.datePosted)
        val extention:TextView=view.findViewById(R.id.extention)

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