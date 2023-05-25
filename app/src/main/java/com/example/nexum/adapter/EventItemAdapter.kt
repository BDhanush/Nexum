package com.example.nexum.adapter

import android.app.*
import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.*
import com.example.nexum.Notification
import com.example.nexum.firebasefunctions.deleteEvent
import com.example.nexum.firebasefunctions.eventFromMap
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.Event
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EventItemAdapter(val dataset:MutableList<Event>): RecyclerView.Adapter<EventItemAdapter.ItemViewHolder>()
{
    lateinit var context: Context
    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val previewImage:ImageView= view.findViewById(R.id.previewImage)
        val title:TextView=view.findViewById(R.id.title)
        val username:TextView=view.findViewById(R.id.username)
        val profilePicture:ShapeableImageView=view.findViewById(R.id.profilePicture)
        val interestButton:MaterialButton=view.findViewById(R.id.interestButton)
        val description:TextView=view.findViewById(R.id.description)
        val date:TextView=view.findViewById(R.id.date)
        val location:TextView=view.findViewById(R.id.location)
        val time:TextView=view.findViewById(R.id.time)
        val eventCard:MaterialCardView=view.findViewById(R.id.eventCard)










    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        context=parent.context

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        if(item.previewImage!=null)
        {
            Picasso.get().load(item.previewImage).into(holder.previewImage);
        }
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
                    Picasso.get().load(user.profilePicture).into(holder.profilePicture);
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
        val alarmManager = holder.itemView.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(!isExpired(holder.interestButton,item.epoch!!)){
            getInterest(holder.interestButton,item)
        }

        holder.eventCard.setOnClickListener {
            val keyEvent = item.key
            val intent = Intent(holder.itemView.context, EventDetailsActivity::class.java)
            intent.putExtra("key", keyEvent)
            holder.itemView.context.startActivity(intent)
        }
        holder.eventCard.setOnLongClickListener {
            val auth=Firebase.auth
            if(auth.currentUser!!.uid==item.uid) {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete ${item.title}")
                    .setMessage("Are you sure you want to delete this event? All media related to ${item.title} will be lost permanently.")
                    .setNegativeButton("Cancel") { dialog, which ->

                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        deleteEvent(item.key!!,context)
                        Toast.makeText(context,"Event Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
            return@setOnLongClickListener true
        }
        holder.interestButton.setOnClickListener {
            if(holder.interestButton.text=="interested")
            {
                holder.interestButton.text = "interest"
                holder.interestButton.icon = ContextCompat.getDrawable(holder.itemView.context,R.drawable.baseline_add_24)
                cancelNotification(alarmManager,"${item.title} at ${item.time}",holder.itemView)
                return@setOnClickListener
            }

            holder.interestButton.text = "interested"
            holder.interestButton.icon = ContextCompat.getDrawable(holder.itemView.context,R.drawable.baseline_check_24)
            fun showAlert(time: Long, title: String, message: String)
            {
                Toast.makeText(holder.itemView.context,"Event Reminder set",Toast.LENGTH_SHORT).show()
            }
            @RequiresApi(Build.VERSION_CODES.O)
            fun scheduleNotification(title:String, date:String, time:String)
            {
                val intent = Intent(holder.itemView.context.applicationContext, Notification::class.java)
                val message = "$title at $time"
                intent.putExtra(titleExtra, "Event Reminder")
                intent.putExtra(messageExtra, message)

                val pendingIntent = PendingIntent.getBroadcast(
                    holder.itemView.context.applicationContext,
                    getNotificationID(message),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val epoch=item.epoch!!-3600000

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    epoch,
                    pendingIntent
                )
                showAlert(epoch, title, message)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun createNotificationChannel()
            {
                val name = "Notif Channel"
                val desc = "Event remainders"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelID, name, importance)
                channel.description = desc
                val notificationManager = holder.itemView.context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            createNotificationChannel()
            scheduleNotification(item.title!!,item.date!!,item.time!!)
        }
    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     */ override fun getItemCount() = dataset.size
}

fun isExpired(interestButton:MaterialButton,epoch:Long):Boolean{
    return if(epoch<System.currentTimeMillis())
    {
        interestButton.text = "expired"
        interestButton.icon = ContextCompat.getDrawable(interestButton.context,R.drawable.outline_timer_24)
        interestButton.isEnabled=false
        true
    }else{
        interestButton.isEnabled=true
        false
    }
}

fun getInterest(interestButton:MaterialButton,event:Event) {
    val message = "${event.title} at ${event.time}"
    if(mapNotificationID.containsKey(message))
    {
        interestButton.text = "interested"
        interestButton.icon = ContextCompat.getDrawable(interestButton.context,R.drawable.baseline_check_24)
    }else{
        interestButton.text = "interest"
        interestButton.icon = ContextCompat.getDrawable(interestButton.context,R.drawable.baseline_add_24)
    }
}

fun cancelNotification(alarmManager: AlarmManager,message:String,view: View)
{
    val intent = Intent(view.context.applicationContext, Notification::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        view.context.applicationContext,
        getNotificationID(message),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
    )
    set.add(mapNotificationID[message]!!)
    mapNotificationID.remove(message)
    alarmManager.cancel(pendingIntent)
    Toast.makeText(view.context,"Event Reminder removed",Toast.LENGTH_SHORT).show()

}