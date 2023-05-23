
package com.example.nexum

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.nexum.adapter.GridViewAdapter
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityAddOpportunityBinding
import com.example.nexum.databinding.EventDetailsBinding

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: EventDetailsBinding
    private lateinit var gridView: GridView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = EventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.event_details)

        gridView = findViewById(R.id.gridView)

        val imageUrls = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b4/Lionel-Messi-Argentina-2022-FIFA-World-Cup_%28cropped%29.jpg",
                "https://dailypost.ng/wp-content/uploads/2023/05/Messi.jpg","https://cdn.britannica.com/35/238335-050-2CB2EB8A/Lionel-Messi-Argentina-Netherlands-World-Cup-Qatar-2022.jpg",
            "https://dailypost.ng/wp-content/uploads/2022/11/3cabcc81540d0491.jpg"
        )
        val adapter = GridViewAdapter(imageUrls)
        gridView.adapter = adapter

//        gridView.setOnItemClickListener { parent, view, position, id ->
//            val imageUrl = imageUrls[position]
//            openImage(imageUrl)
//        }

    }
//    private fun openImage(imageUrl: String) {
//        // Handle the image click event here based on your desired implementation.
//        // For example, you can start a new activity and pass the image URL as an intent extra.
//        // Then, in the new activity, you can load and display the image.
//        val intent = Intent(this, DisplayImageActivity::class.java)
//        //intent.putExtra("imageUrl", imageUrl)
//        startActivity(intent)
//    }

}

