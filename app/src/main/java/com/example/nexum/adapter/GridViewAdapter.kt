package com.example.nexum.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nexum.DisplayImageActivity
import com.example.nexum.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle.parent

class GridViewAdapter(private val dataset: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataset.size
    }

    override fun getItem(position: Int): Any {
        return dataset[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflate the grid_item layout
            view = LayoutInflater.from(parent?.context).inflate(R.layout.grid_item, parent, false)

            // Create a ViewHolder to hold references to the views
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.imageView)

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Load the image using Glide or Picasso
        parent?.context?.let {
            Glide.with(it)
                .load(dataset[position])
                .into(viewHolder.imageView)
        }

        // Set click listener for the ImageView
        viewHolder.imageView.setOnClickListener {
            val intent = Intent(parent?.context, DisplayImageActivity::class.java)
            intent.putExtra("imageUrl", dataset[position])
            parent?.context?.startActivity(intent)
        }

        return view
    }

    // ViewHolder class to hold references to the views
    private class ViewHolder {
        lateinit var imageView: ImageView
    }



}