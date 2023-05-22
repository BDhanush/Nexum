package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Opportunity(val uid:String?=null,val title:String?=null,val description:String?=null,val link:String?=null,val datePosted:Long=System.currentTimeMillis()) {
    var key:String?=null

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "link" to link,
            "datePosted" to datePosted
        )
    }

}