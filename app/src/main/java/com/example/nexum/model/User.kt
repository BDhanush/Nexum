package com.example.nexum.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.android.play.integrity.internal.w
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.ByteArrayOutputStream
import java.lang.System.out

@IgnoreExtraProperties
class User(val firstName:String?=null,val lastName:String?=null,val email:String?=null,var profilePicture:String?=null) {
    var uid:String?=null
    init{


    }
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "profilePicture" to profilePicture
        )
    }

}