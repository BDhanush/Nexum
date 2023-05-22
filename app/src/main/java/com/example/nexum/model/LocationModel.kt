package com.example.nexum.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import java.math.BigInteger
import java.security.MessageDigest
@IgnoreExtraProperties
data class LocationModel(val latLong: LatLng) {

    fun push(uid:String)
    {
        val key:String=getKey(uid)
        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("location")
        database.child(key).setValue(latLong)
    }
    private fun getKey(uid:String):String
    {
        val md = MessageDigest.getInstance("SHA-256")
        val messageDigest = md.digest(uid.toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        return hashtext

    }
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "latLong" to latLong,

        )
    }
}