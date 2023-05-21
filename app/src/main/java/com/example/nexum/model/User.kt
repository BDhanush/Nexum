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
import kotlinx.coroutines.processNextEventInCurrentThread

@IgnoreExtraProperties
class User(val firstName:String?=null,val lastName:String?=null,val email:String?=null) {
    var profilePicture:String?=null
    init{
        val baseColor=Color.WHITE
        val red = (baseColor.red+(0..256).random())/2
        val green = (baseColor.green+(0..256).random())/2
        val blue = (baseColor.blue+(0..256).random())/2
        val color = Color.rgb(red,green,blue)

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