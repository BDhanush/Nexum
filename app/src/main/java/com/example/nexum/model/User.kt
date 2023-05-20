package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class User(val firstName:String?=null,val lastName:String?=null,val email:String?=null) {
    var profilePicture:String?=null
    init{

    }
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
        )
    }

}