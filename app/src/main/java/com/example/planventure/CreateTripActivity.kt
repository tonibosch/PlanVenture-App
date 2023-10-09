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
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class CreateTripActivity : AppCompatActivity() {

    private lateinit var tripName: EditText
    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var DateRangePickerButton: Button
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView

    //services
    private lateinit var tripService: TripService

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
        DateRangePickerButton = findViewById(R.id.dateRangePickerButton)
        tvStartDate = findViewById(R.id.startDate_textView)
        tvEndDate = findViewById(R.id.endDate_textView)

        tripService = TripService(applicationContext)

        addButton.setOnClickListener {
            if(!checksNullValues()) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val trip = Trip(1,
                    tripName.text.toString(),
                    formatter.parse(tvStartDate.text.toString()),
                    formatter.parse(tvEndDate.text.toString()),
                    location.text.toString(),
                    maxNumberOfParts.text.toString().toInt(),
                    "Beschreibung",
                    ArrayList(),
                    ArrayList(),
                    TRIP_STATE.CLOSED
                )
                setResult(Activity.RESULT_OK)
                tripService.addTrip(trip)
                this.finish()
            }
        }

        cancelButton.setOnClickListener {
            // Only for checking ... needs to be deleted later
            tripService.deleteAllTrips()
            val trips = tripService.getAllTrips()
            Log.d("ALL_TRIPS", trips.size.toString())
            for(t in trips) Log.d("TRIP", t.toString())
            this.finish()
        }

        backButton.setOnClickListener {
            this.finish()
        }

        DateRangePickerButton.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun checksNullValues():Boolean {
        if (tripName.text.isEmpty()) {
            Toast.makeText(this, "Enter a trip name", Toast.LENGTH_SHORT).show()
            return true
        } else if(tvStartDate.text == "Select start date"){
            Toast.makeText(this, "Select dates for the trip.", Toast.LENGTH_SHORT).show()
            return true
        } else if (location.text.isEmpty()) {
            Toast.makeText(this, "Enter a destination", Toast.LENGTH_SHORT).show()
            return true
        }  else if (maxNumberOfParts.text.isEmpty()) {
            Toast.makeText(this, "Enter the number of participants", Toast.LENGTH_SHORT).show()
            return true
        } else return false
    }

    private fun showDateRangePicker() {
        val dateRangerPicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Date").build()
        dateRangerPicker.show(supportFragmentManager,"date_range_picker")
        dateRangerPicker.addOnPositiveButtonClickListener { datePicked->
            val firstDate = datePicked.first
            val secondDate = datePicked.second

            if(firstDate != null && secondDate != null) {
                tvStartDate.text = convertLongToDate(firstDate)
                tvEndDate.text = convertLongToDate(secondDate)
            }
        }
    }

    private fun convertLongToDate(time:Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

}