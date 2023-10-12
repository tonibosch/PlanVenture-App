package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.P)
class TripInformationActivity : AppCompatActivity() {

    private lateinit var tripName: EditText
    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var backButton: ImageButton
    private lateinit var dateRangePickerButton: Button
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var updateTripBtn: Button

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
        updateTripBtn = findViewById(R.id.updateTripButton)

        tripName.text = Editable.Factory.getInstance().newEditable(trip?.getName())

        backButton.setOnClickListener{
            this.finish()
        }

        dateRangePickerButton.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager,tvStartDate, tvEndDate)
            datePicker.showDateRangePicker()
        }

        updateTripBtn.text = "Update"

        updateTripBtn.setOnClickListener {

            setResult(Activity.RESULT_OK)
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                tripService.updateTrip(idTrip, Trip(1, tripName.text.toString(), formatter.parse(tvStartDate.text.toString()),
                    formatter.parse(tvEndDate.text.toString()), location.text.toString(), maxNumberOfParts.text.toString().toInt(),
                    "Description", ArrayList(), ArrayList(), TRIP_STATE.PLANNING))
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}