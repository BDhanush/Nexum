package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class SharedFile(val uid:String?=null, val title:String?=null, var fileURL:String?=null,val datePosted:Long=System.currentTimeMillis()) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "fileURL" to fileURL,
            "datePosted" to datePosted
        )
    }

}