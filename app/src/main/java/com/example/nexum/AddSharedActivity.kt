package com.example.nexum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityAddOpportunityBinding
import com.example.nexum.databinding.ActivityAddSharedBinding
import java.io.File

class AddSharedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSharedBinding
    val SELECT_FILE=200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSharedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.selectFile.setOnClickListener {
            addFile()
        }
    }

    private fun addFile() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
    }

    @SuppressLint("Range")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==SELECT_FILE)
        {

            binding.renameInput.setText((data!!.data).toString())
            val cursor = contentResolver.query(data.data!!, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val fileName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                binding.fileName.setText("Selected File: $fileName")
                binding.renameInput.setText(fileName.dropLastWhile { it!='.' }.dropLast(1))
            }

            binding.renameLayout.visibility= View.VISIBLE
            binding.fileName.visibility= View.VISIBLE

        }
    }
}