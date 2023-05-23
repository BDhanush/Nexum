package com.example.nexum

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

val mapNotificationID:MutableMap<String,Int> = mutableMapOf()
val set:MutableSet<Int> = mutableSetOf(1)
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

fun getNotificationID(name:String):Int
{
    if(mapNotificationID.containsKey(name)) {
        return mapNotificationID[name]!!
    }

    if(set.isEmpty())
    {
        mapNotificationID[name] = mapNotificationID.size+1
    }else {
        mapNotificationID[name] = set.elementAt(0)
        set.remove(set.elementAt(0))
    }
    return mapNotificationID[name]!!

}

class Notification : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(mapNotificationID[intent.getStringExtra(messageExtra)]!!, notification)
        set.add(mapNotificationID[intent.getStringExtra(messageExtra)]!!)
        mapNotificationID.remove(intent.getStringExtra(messageExtra))
    }

}