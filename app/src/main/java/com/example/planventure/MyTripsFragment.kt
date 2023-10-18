package com.example.planventure

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var scrollLayout: LinearLayout
    private lateinit var spinnerStatus: Spinner
    private var statusSelected = "ALL"

    //services
    private lateinit var tripService: TripService

    companion object {
        private const val CREATE_TRIP_REQUEST = 1 // You can choose any integer value
        const val TRIP_ID_TRIP_INFORMATION = "com.example.planventure.MyTripsFragment.tripId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_trips, container, false)
        tripService = TripService(view.context)

        //Configure button Create Trip
        button = view.findViewById(R.id.button2)
        button.setOnClickListener {
            val intent = Intent(container!!.context, CreateTripActivity::class.java)
            startActivityForResult(intent, CREATE_TRIP_REQUEST)
        }

        //Configure Spinner Trip Status
        spinnerStatus = view.findViewById(R.id.spinnerStatus)
        val listTripStatus = arrayOf(
            "ALL",
            TRIP_STATE.PLANNING.toString(),
            TRIP_STATE.STARTED.toString(),
            TRIP_STATE.FINISHED.toString()
        )
        var adapter: ArrayAdapter<String> =
            ArrayAdapter(container!!.context, android.R.layout.simple_spinner_item, listTripStatus)
        spinnerStatus.adapter = adapter
        spinnerStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                statusSelected = spinnerStatus.selectedItem.toString()
                Log.d("statusSelected", statusSelected)
                refreshTripList()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
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
        var trips: List<Trip>? = null
        if (statusSelected == TRIP_STATE.PLANNING.toString()) trips =
            tripService.getTripsByState(TRIP_STATE.PLANNING)
        else if (statusSelected == TRIP_STATE.STARTED.toString()) trips =
            tripService.getTripsByState(TRIP_STATE.STARTED)
        else if (statusSelected == TRIP_STATE.FINISHED.toString()) trips =
            tripService.getTripsByState(TRIP_STATE.FINISHED)
        else trips = tripService.getAllTrips()
        for (t in trips) Log.d("TRIP_WITH_FILTER: $statusSelected", "$t")
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout?.removeAllViews()

        var i = 1
        if (trips != null) {
            for (trip in trips) {
                val textView =
                    TextView(context)                                // Create a new TextView for each trip
                val formattedText =
                    "       <b>${trip.getName()}</b><br>" +
                            "       Location: ${trip.getLocation()}<br>" +
                            "       from ${convertDate(trip.getStartDate())} to" +
                            " ${convertDate(trip.getEndDate())}"
                textView.text = Html.fromHtml(formattedText, Html.FROM_HTML_MODE_COMPACT)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                if (i % 2 == 0) textView.setBackgroundColor(Color.WHITE)        // Set the background color based on whether i is odd or even
                else textView.setBackgroundColor(Color.LTGRAY)

                textView.setOnClickListener {
                    val intent = Intent(this.context, TripInformationActivity::class.java)
                    intent.putExtra(TRIP_ID_TRIP_INFORMATION, trip.getId())
                    startActivity(intent)
                }

                linearLayout?.addView(textView)
                ++i
            }
        }
    }

    private fun convertDate(date: Date): String? {
        return try {
            val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            desiredFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "Error during Date conversion"
        }
    }
}