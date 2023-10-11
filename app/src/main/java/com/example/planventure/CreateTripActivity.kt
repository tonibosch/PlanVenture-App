package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.service.TripService
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateTripActivity : AppCompatActivity() {

    private lateinit var tripName: EditText
    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var dateRangePickerButton: Button
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView

    //services
    private lateinit var tripService: TripService

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        tripName = findViewById(R.id.tripName_editText)
        location = findViewById(R.id.location_editText)
        maxNumberOfParts = findViewById(R.id.maxPartNumber_editText)
        addButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        backButton = findViewById(R.id.backButton)
        dateRangePickerButton = findViewById(R.id.dateRangePickerButton)
        tvStartDate = findViewById(R.id.startDate_textView)
        tvEndDate = findViewById(R.id.endDate_textView)

        tripService = TripService(applicationContext)

        addButton.setOnClickListener {

            setResult(Activity.RESULT_OK)
            try {
                //creates list with data for the trip
                val myData = mutableListOf<String>()
                myData.add(tripName.text.toString())
                myData.add(tvStartDate.text.toString())
                myData.add(tvEndDate.text.toString())
                myData.add(location.text.toString())
                myData.add(maxNumberOfParts.text.toString())
                myData.add("Description")
                tripService.addTrip(
                    myData
                )
                this.finish()
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }


        cancelButton.setOnClickListener {
            // Only for checking ... needs to be deleted later
            tripService.deleteAllTrips()
            val trips = tripService.getAllTrips()
            Log.d("ALL_TRIPS", trips.size.toString())
            for (t in trips) Log.d("TRIP", t.toString())
            this.finish()
        }

        backButton.setOnClickListener {
            this.finish()
        }

        dateRangePickerButton.setOnClickListener {
            showDateRangePicker()
        }
    }

    /*private fun checksNullValues(): Boolean {
        if (tripName.text.isEmpty()) {
            Toast.makeText(this, "Enter a trip name", Toast.LENGTH_SHORT).show()
            return true
        } else if (tvStartDate.text == "Select start date") {
            Toast.makeText(this, "Select dates for the trip.", Toast.LENGTH_SHORT).show()
            return true
        } else if (location.text.isEmpty()) {
            Toast.makeText(this, "Enter a destination", Toast.LENGTH_SHORT).show()
            return true
        } else if (maxNumberOfParts.text.isEmpty()) {
            Toast.makeText(this, "Enter the number of participants", Toast.LENGTH_SHORT).show()
            return true
        } else return false
    }*/

    private fun showDateRangePicker() {
        val dateRangerPicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Date").build()
        dateRangerPicker.show(supportFragmentManager, "date_range_picker")
        dateRangerPicker.addOnPositiveButtonClickListener { datePicked ->
            val firstDate = datePicked.first
            val secondDate = datePicked.second

            if (firstDate != null && secondDate != null) {
                tvStartDate.text = convertLongToDate(firstDate)
                tvEndDate.text = convertLongToDate(secondDate)
            }
        }
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

}