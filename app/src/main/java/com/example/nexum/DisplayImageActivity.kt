
package com.example.nexum

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide


class DisplayImageActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        imageView = findViewById(R.id.imageView)

        val imageUrl = intent.getStringExtra("imageUrl")

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl.toUri())
                .into(imageView)
        }
    }
}
