package com.example.nexum

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityAddOpportunityBinding

class EventDetailsActivity : AppCompatActivity() {

//    private lateinit var binding: EventDetails

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        binding = ActivityAddOpportunityBinding.inflate(layoutInflater)
//        val view = binding.root
        setContentView(R.layout.event_details)


    }

}
