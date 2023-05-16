package com.example.nexum

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.databinding.ActivityAddEventBinding

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

}
