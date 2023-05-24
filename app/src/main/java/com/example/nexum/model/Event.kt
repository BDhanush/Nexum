package com.example.nexum.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@IgnoreExtraProperties
data class Event(val uid:String?=null, val title:String?=null, val description:String?=null, val venue:String?=null,val date:String?,val time:String?){
    val interested:MutableList<String> = mutableListOf()
    var previewImage:String?=null
    val images:MutableList<String?> = mutableListOf()
    var key:String?=null
    var epoch:Long?=null

    @RequiresApi(Build.VERSION_CODES.O)
    fun setEpoch()
    {
        val epochString = this.date+" "+this.time
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a").withZone(
            ZoneId.of(ZoneId.systemDefault().id))
        val zdt: ZonedDateTime = ZonedDateTime.parse(epochString,dtf)
        epoch = zdt.toInstant().toEpochMilli()
    }
    init{

    }
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "venue" to venue,
            "date" to date,
            "time" to time,
            "previewImage" to previewImage,
            "images" to images
        )
    }
}