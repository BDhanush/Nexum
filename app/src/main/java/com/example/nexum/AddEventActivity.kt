package com.example.nexum

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.model.Event
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private val SELECT_PICTURE = 200;
    private var selectedImageUri: Uri?=null;
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var date:String?=null;
        var time:String?=null;

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        binding.dateAndTime.setOnClickListener {

            datePicker.show(supportFragmentManager,"Select date")

        }
        datePicker.addOnPositiveButtonClickListener {
            timePicker.show(supportFragmentManager,"Select time")
            date=datePicker.headerText
        }
        timePicker.addOnPositiveButtonClickListener{
            val formatter = SimpleDateFormat("h:mm a");

            time=formatter.format(Time(timePicker.hour,timePicker.minute,0))
            binding.dateText.text =  date!! + " " + time!!
            binding.dateText.visibility= View.VISIBLE
        }

        binding.addImage.setOnClickListener {
            chooseImage()
        }

        binding.submit.setOnClickListener {
            val auth = Firebase.auth
            val event=Event(
                auth.currentUser!!.uid,
                binding.eventNameInput.text.toString().trim(),
                binding.descriptionInput.text.toString().trim(),
                binding.venueInput.text.toString().trim(),
                date,
                time
            )
            if(checkFields()) {
                uploadEvent(event)
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun uploadEvent(event:Event)
    {
        Toast.makeText(this,"Adding Event",Toast.LENGTH_SHORT).show()
        val database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val key = database.child("events").push().key
        val storageRef = Firebase.storage

        val ref = storageRef.reference.child("images/$key/previewImage")

        val uploadTask = if(selectedImageUri!=null) {
            // Compress the image before uploading
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val tempFile = File.createTempFile("temp", null)
            tempFile.deleteOnExit()
            FileOutputStream(tempFile).use { fileOutputStream ->
                val buffer = ByteArray(4 * 1024) // Adjust the buffer size as needed
                var bytesRead: Int
                if (inputStream != null) {
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            val bitmap = Compressor(this).compressToBitmap(tempFile)
            val compressedBitmap = compressBitmap(bitmap, 500) // Adjust the desired max image size here
            val baos = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos) // Adjust the desired compression quality here
            val data = baos.toByteArray()

            ref.putBytes(data)
        }else{
            val baseColor = Color.WHITE
            val red = (baseColor.red + (0..256).random()) / 2
            val green = (baseColor.green + (0..256).random()) / 2
            val blue = (baseColor.blue + (0..256).random()) / 2
            val color = Color.rgb(red, green, blue)

            val bmp: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            val canvas = Canvas(bmp);
            canvas.drawColor(color)

            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.WEBP, 0, baos)
            val data = baos.toByteArray()

            ref.putBytes(data)
        }

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                event.previewImage=downloadUri.toString()
                database.child("events").child(key!!).setValue(event)
                Toast.makeText(this,"Event added",Toast.LENGTH_SHORT).show()
                finish()
            }
        }


    }
    private fun compressBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val aspectRatio: Float = width.toFloat() / height.toFloat()

        if (width > maxSize || height > maxSize) {
            if (aspectRatio > 1) {
                width = maxSize
                height = (width / aspectRatio).toInt()
            } else {
                height = maxSize
                width = (height * aspectRatio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
    private fun checkFields():Boolean
    {
        var check:Boolean=true;
        if (binding.eventNameInput.length() == 0) {
            binding.eventNameInput.error = "This field is required"
            check = false
        }
        if (binding.descriptionInput.length() == 0) {
            binding.descriptionInput.error = "This field is required"
            check = false
        }
        if (binding.dateText.visibility!=View.VISIBLE) {
            binding.dateAndTime.error = "Select date and time"
            check = false
        }
        if (binding.venueInput.text.toString().isEmpty()) {
            binding.venueInput.error = "This field is required"
            check = false
        }
        // after all validation return true.
        return check

    }
    private fun chooseImage()
    {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==SELECT_PICTURE)
        {
            selectedImageUri = data!!.data

            if(selectedImageUri!=null)
            {
                binding.addImage.text="Change Preview Image"
                binding.addImage.icon= ContextCompat.getDrawable(this,R.drawable.baseline_check_24)
                binding.previewImage.setImageURI(selectedImageUri)
                binding.noPreview.visibility=View.GONE
            }else{
                binding.addImage.text="Add Image"
                binding.addImage.icon= ContextCompat.getDrawable(this,R.drawable.baseline_add_24)
                binding.previewImage.setImageURI(null)
                binding.noPreview.visibility=View.VISIBLE
            }

        }
    }

}
