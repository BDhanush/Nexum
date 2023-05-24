package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class SharedImage(val uid:String?=null, var imageURL:String?=null, val datePosted:Long=System.currentTimeMillis()) {
    var eventKey:String?=null
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "imageURL" to imageURL,
            "datePosted" to datePosted
        )
    }

}