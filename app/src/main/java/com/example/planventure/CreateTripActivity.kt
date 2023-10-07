package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.icu.util.LocaleData
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.P)
class CreateTripActivity : AppCompatActivity() {

    private lateinit var tripName: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton


    //services
    private lateinit var tripService: TripService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        tripName = findViewById(R.id.tripName_editText)
        startDate = findViewById(R.id.startDate_editText)
        endDate = findViewById(R.id.endDate_editText)
        location = findViewById(R.id.location_editText)
        maxNumberOfParts = findViewById(R.id.maxPartNumber_editText)
        addButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        backButton = findViewById(R.id.backButton)


        tripService = TripService(applicationContext)

        addButton.setOnClickListener {

            val formatter = SimpleDateFormat("yyyy-MM-dd")

            val trip = Trip(1,
                tripName.text.toString(),
                formatter.parse(startDate.text.toString()),
                formatter.parse(endDate.text.toString()),
                location.text.toString(),
                maxNumberOfParts.text.toString().toInt(),
                "Beschreibung",
                ArrayList(),
                ArrayList(),
                TRIP_STATE.CLOSED
                )

            Log.d("OUR GLORIUS TRIP", trip.toString())

            setResult(Activity.RESULT_OK)
            tripService.addTrip(trip)

            this.finish()
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

    }

}