package com.example.nexum.adapter

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.SeekBar.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.R
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.SharedFile
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.sql.Date
import java.text.SimpleDateFormat


class SharedItemAdapter(val dataset:MutableList<SharedFile>): RecyclerView.Adapter<SharedItemAdapter.ItemViewHolder>()
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

        holder.fileName.text=item.title!!.dropLastWhile { it!='.' }.dropLast(1)
        holder.datePosted.text= SimpleDateFormat("dd/MM/yy hh:mm a").format(Date(item.datePosted))
        holder.extention.text=item.title.dropWhile { it!='.' }

        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val userMap=dataSnapshot.child(item.uid!!).value as Map<String,Any?>
                val user = userFromMap(userMap)
                holder.username.text = user.firstName + " " + user.lastName
                if(user.profilePicture!=null)
                    Picasso.get().load(user.profilePicture).into(holder.profilePicture);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        holder.downloadButton.setOnClickListener {
            val manager = holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val uri: Uri = Uri.parse(item.fileURL)
            val request = DownloadManager.Request(uri)
            request.setTitle(item.title)
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, item.title);
            val reference: Long = manager!!.enqueue(request)
        }

    }
    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}