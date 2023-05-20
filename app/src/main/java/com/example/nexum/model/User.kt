package com.example.nexum.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.android.play.integrity.internal.w


class User(val firstName:String?=null,val lastName:String?=null,val email:String?=null) {
    var profilePicture:String?=null
    init{
        val baseColor=Color.WHITE
        val red = (baseColor.red+(0..256).random())/2
        val green = (baseColor.green+(0..256).random())/2
        val blue = (baseColor.blue+(0..256).random())/2
        val color = Color.rgb(red,green,blue)

        val bitmap: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color)
    }
}