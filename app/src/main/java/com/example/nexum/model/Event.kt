package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Event(val uid:String?=null, val title:String?=null, val description:String?=null, val venue:String?=null,val date:String?,val time:String?){
    val interested:MutableList<String> = mutableListOf()
    var previewImage:String?=null

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
            "previewImage" to previewImage
        )
    }
}