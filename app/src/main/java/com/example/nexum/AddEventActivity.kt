package com.example.nexum

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    }

}
