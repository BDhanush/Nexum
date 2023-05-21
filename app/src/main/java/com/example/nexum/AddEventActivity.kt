package com.example.nexum

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nexum.databinding.ActivityAddEventBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private val SELECT_PICTURE = 200;
    private var selectedImageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var date:String?=null;

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

            binding.dateText.text =  date!! + " " + formatter.format(Time(timePicker.hour,timePicker.minute,0))
            date=binding.dateText.text.toString()
            binding.dateText.visibility= View.VISIBLE
        }

        binding.addImage.setOnClickListener {
            chooseImage()
        }

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
                binding.addImage.icon = ContextCompat.getDrawable(this, R.drawable.baseline_add_24)
            }

        }
    }

}
