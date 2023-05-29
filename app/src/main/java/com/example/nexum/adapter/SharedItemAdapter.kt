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
import com.bumptech.glide.Glide
import com.example.nexum.R
import com.example.nexum.firebasefunctions.deleteEvent
import com.example.nexum.firebasefunctions.deleteShared
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.SharedFile
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.sql.Date
import java.text.SimpleDateFormat


class SharedItemAdapter(val dataset:MutableList<SharedFile>): RecyclerView.Adapter<SharedItemAdapter.ItemViewHolder>()
{
    lateinit var context:Context
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val fileName:TextView=view.findViewById(R.id.fileName)
        val username:TextView=view.findViewById(R.id.username)
        val profilePicture: ShapeableImageView =view.findViewById(R.id.profilePicture)
        val downloadButton:Button=view.findViewById(R.id.downloadButton)
        val datePosted:TextView=view.findViewById(R.id.datePosted)
        val extention:TextView=view.findViewById(R.id.extention)
        val sharedCard: MaterialCardView =view.findViewById(R.id.sharedCard)

        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        context=parent.context
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
                    Glide.with(holder.itemView.context).load(user.profilePicture!!.toUri()).into(holder.profilePicture)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        holder.sharedCard.setOnLongClickListener {
            val auth= Firebase.auth
            if(auth.currentUser!!.uid==item.uid) {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete ${item.title}")
                    .setMessage("Are you sure you want to delete this file? All data related to ${item.title} will be lost permanently.")
                    .setNegativeButton("Cancel") { dialog, which ->

                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        deleteShared(item.key!!,context)
                        Toast.makeText(context,"File Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
            return@setOnLongClickListener true
        }

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