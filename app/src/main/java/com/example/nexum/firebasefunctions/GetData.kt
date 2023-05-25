package com.example.nexum.firebasefunctions

import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.nexum.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.concurrent.CountDownLatch

fun fileFromMap(map:Map<String,Any?>): SharedFile
{
    return SharedFile(map["uid"] as String,map["title"] as String,map["fileURL"] as String,map["datePosted"] as Long)
}

fun imageFromMap(map:Map<String,Any?>): SharedImage
{
    return SharedImage(map["uid"] as String,map["imageURL"] as String,map["datePosted"] as Long)
}

fun oppoFromMap(map:Map<String,Any?>): Opportunity
{
    return Opportunity(map["uid"] as String,map["title"] as String,map["description"] as String,map["link"] as String,map["datePosted"] as Long)
}

@RequiresApi(Build.VERSION_CODES.O)
fun eventFromMap(map:Map<String,Any?>): Event
{
    val event= Event(map["uid"] as String,map["title"] as String,map["description"] as String,map["venue"] as String,map["date"] as String,map["time"] as String)
    event.previewImage=map["previewImage"] as String?
    event.key=map["key"] as String?
    event.setEpoch()
    return event
}

fun locationFromMap(map:Map<String,Any?>): LocationModel
{
    return LocationModel(LatLng( map["latitude"] as Double,map["longitude"] as Double))
}

fun deleteEvent(eventKey:String,context:Context)
{
    val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("events/$eventKey")
    database.removeValue()

    val storageRef = Firebase.storage.reference
    val desertRef = storageRef.child("images/$eventKey")

    desertRef.delete().addOnSuccessListener {
//        Toast.makeText(context,"Event Deleted", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {
        // Uh-oh, an error occurred!
    }

}
fun deleteShared(sharedKey:String,context:Context)
{
    val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("files/$sharedKey")
    database.removeValue()

    val storageRef = Firebase.storage.reference
    val desertRef = storageRef.child("files/$sharedKey")

    desertRef.delete().addOnSuccessListener {
//        Toast.makeText(context,"File Deleted", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {
        // Uh-oh, an error occurred!
    }

}

fun deleteOppo(oppoKey:String,context: Context)
{
    val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("opportunities/$oppoKey")
    database.removeValue().addOnSuccessListener {
//        Toast.makeText(context,"Opportunity Deleted", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {
        // Uh-oh, an error occurred!
    }
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






