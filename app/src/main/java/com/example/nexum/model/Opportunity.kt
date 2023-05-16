package com.example.nexum.model

class Opportunity(val uid:String?=null,val title:String?=null,val description:String?=null,val link:String?=null) {
    val datePosted:Long=System.currentTimeMillis()

}