package com.example.nexum.model

class Event(val uid:String?=null, val title:String?=null, val description:String?=null, val venue:String?=null,val date:String?,val time:String?) {
    val interested:MutableList<String> = mutableListOf()
    var previewImage:String?=null
    init{

    }

}