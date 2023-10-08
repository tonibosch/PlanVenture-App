package com.example.planventure

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.planventure.service.TripService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var scrollLayout: LinearLayout

    //services
    private lateinit var tripService: TripService

    companion object {
        private const val CREATE_TRIP_REQUEST = 1 // You can choose any integer value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_trips, container, false)
        tripService = TripService(view.context)

        button = view.findViewById(R.id.button2)
        button.setOnClickListener {
            val intent = Intent(container!!.context, CreateTripActivity::class.java)
            startActivityForResult(intent, CREATE_TRIP_REQUEST)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshTripList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_TRIP_REQUEST && resultCode == Activity.RESULT_OK) {
            refreshTripList()           //When a new trip has been created, refresh the list of trips
        }
    }

    private fun refreshTripList() {
        val trips = tripService.getAllTrips()
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linearLayout)

        var i = 1
        for (trip in trips) {
            val textView = TextView(context)                                // Create a new TextView for each trip
            textView.text = "       Name: ${trip.getName()}\n       Location: ${trip.getLocation()}\n       from " + convertDate(trip.getStartDate()) + " to " + convertDate(trip.getEndDate())
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            if (i % 2 == 0) textView.setBackgroundColor(Color.WHITE)        // Set the background color based on whether i is odd or even
            else textView.setBackgroundColor(Color.LTGRAY)
            linearLayout?.addView(textView)
            ++i
        }
    }

    private fun convertDate(date: Date): String? {
        try {
            val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return desiredFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error during Date conversion"
        }
    }
}