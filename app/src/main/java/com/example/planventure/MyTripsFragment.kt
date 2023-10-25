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
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.databinding.FragmentMyTripsBinding
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import com.example.planventure.utility.SwipeGesture
import com.example.planventure.utility.TripAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var spinnerStatus: Spinner
    private var statusSelected = "ALL"

    //services
    private lateinit var tripService: TripService

    private lateinit var recyclerView: RecyclerView
    private var trips: ArrayList<Trip> = ArrayList()
    private lateinit var tripAdapter: TripAdapter


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


        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        //tripService.upgrade()

        //Configure button Create Trip
        button = view.findViewById(R.id.button2)
        button.setOnClickListener {
            val intent = Intent(container!!.context, CreateTripActivity::class.java)
            startActivityForResult(intent, CREATE_TRIP_REQUEST)
            Log.d("Columns in trip_table", "Number: ${tripService.getNumberOfColumns()}")
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

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        refreshTripList()
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_TRIP_REQUEST && resultCode == Activity.RESULT_OK) {
            refreshTripList()           //When a new trip has been created, refresh the list of trips
        }
    }

    private fun refreshTripList() {
        if (statusSelected == TRIP_STATE.PLANNING.toString()) trips = tripService.getTripsByState(TRIP_STATE.PLANNING)
        else if (statusSelected == TRIP_STATE.STARTED.toString()) trips = tripService.getTripsByState(TRIP_STATE.STARTED)
        else if (statusSelected == TRIP_STATE.FINISHED.toString()) trips = tripService.getTripsByState(TRIP_STATE.FINISHED)
        else trips = tripService.getAllTrips() as ArrayList<Trip>

        tripAdapter = TripAdapter(trips, this.requireContext())

        val swipegesture = object : SwipeGesture(this.requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        tripAdapter.deleteTrip(viewHolder.adapterPosition)
                    }
                    ItemTouchHelper.RIGHT -> {
                        tripAdapter.archiveTrip(viewHolder.adapterPosition)
                    }

                }
            }
        }
        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = tripAdapter
        tripAdapter.onItemClick = {
            val intent = Intent(this.context, TripInformationActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_INFORMATION, it.getId())
            startActivity(intent)
        }
    }

}