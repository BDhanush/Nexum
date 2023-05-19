package com.example.nexum.model

class SharedFile(val uid:String?=null, val title:String?=null, val fileURL:String?=null) {
    val datePosted:Long=System.currentTimeMillis()

}