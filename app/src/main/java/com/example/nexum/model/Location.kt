package com.example.nexum.model

import com.google.firebase.database.FirebaseDatabase
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class Location(val latLong: Long) {

    fun push(uid:String)
    {
        val key:String=getKey(uid)
        val database = FirebaseDatabase.getInstance().reference
        database.child("location").child(key).setValue(latLong)
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
}