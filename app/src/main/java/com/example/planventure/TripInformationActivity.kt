package com.example.planventure

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker
@RequiresApi(Build.VERSION_CODES.P)
class TripInformationActivity : AppCompatActivity() {

    private lateinit var tripName: EditText
    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var backButton: ImageButton
    private lateinit var dateRangePickerButton: Button
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView

    private lateinit var tripService: TripService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_information)
        tripService = TripService(applicationContext)

        val idTrip = intent.getLongExtra(MyTripsFragment.TRIP_ID_TRIP_INFORMATION,0)
        val trip = tripService.getTripById(idTrip)
        println("SDASDSADSADSA" + idTrip)

        backButton = findViewById(R.id.backButton_TripInfo)
        dateRangePickerButton = findViewById(R.id.dateRangePickerButton_tripInformation)
        tvStartDate = findViewById(R.id.startDate_textView_tripInformation)
        tvEndDate = findViewById(R.id.endDate_textView_tripInformation)
        tripName = findViewById(R.id.tripName_editText_tripInformation)
        location = findViewById(R.id.location_editText_tripInformation)
        maxNumberOfParts = findViewById(R.id.maxPartNumber_editText_tripInformation)

        tripName.text = Editable.Factory.getInstance().newEditable(trip?.getName())

        backButton.setOnClickListener{
            this.finish()
        }

        dateRangePickerButton.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager,tvStartDate, tvEndDate)
            datePicker.showDateRangePicker()
        }

    }
}