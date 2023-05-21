package com.example.nexum.firebasefunctions

import android.content.ContentValues
import android.util.Log
import com.example.nexum.model.Event
import com.example.nexum.model.Opportunity
import com.example.nexum.model.SharedFile
import com.example.nexum.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CountDownLatch

fun fileFromMap(map:Map<String,Any?>): SharedFile
{
    return SharedFile(map["uid"] as String,map["title"] as String,map["fileURL"] as String,map["datePosted"] as Long)
}

fun oppoFromMap(map:Map<String,Any?>): Opportunity
{
    return Opportunity(map["uid"] as String,map["title"] as String,map["description"] as String,map["link"] as String,map["datePosted"] as Long)
}

fun eventFromMap(map:Map<String,Any?>): Event
{
    val event= Event(map["uid"] as String,map["title"] as String,map["description"] as String,map["venue"] as String,map["date"] as String,map["time"] as String)
    event.previewImage=map["previewImage"] as String?
    return event
}

fun userFromMap(map:Map<String,Any?>): User
{
    return User(map["firstName"] as String,map["lastName"] as String,map["email"] as String,map["profilePicture"] as String?)
}

fun readUser(uid:String):User?
{
    var user:User?=null
    var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val userMap=dataSnapshot.child(uid).value as Map<String,Any?>
            user= userFromMap(userMap)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    })

    return user
}

fun readOppo(key:String):Opportunity?
{
    var opportunity:Opportunity?=null
    var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("opportunities")
    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val map=dataSnapshot.child(key).value as Map<String,Any?>
            opportunity=oppoFromMap(map)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    })
    return opportunity
}

fun readShare(key:String):SharedFile?
{
    var sharedFile:SharedFile?=null
    var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("files")
    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val map=dataSnapshot.child(key).value as Map<String,Any?>
            sharedFile=fileFromMap(map)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    })
    return sharedFile
}






