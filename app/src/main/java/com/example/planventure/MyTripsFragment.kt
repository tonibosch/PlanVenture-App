package com.example.planventure

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.databinding.FragmentMyTripsBinding
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.TripService
import com.example.planventure.utility.SwipeGesture
import com.example.planventure.utility.TripAdapter
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {

    private lateinit var binding: FragmentMyTripsBinding
    private var statusSelected = "ALL"
    private var trips: ArrayList<Trip> = ArrayList()
    //Services
    private lateinit var tripService: TripService
    //Adapters
    private lateinit var tripAdapter: TripAdapter

    companion object {
        private const val CREATE_TRIP_REQUEST = 1 // You can choose any integer value
        const val TRIP_ID_TRIP_INFORMATION = "com.example.planventure.MyTripsFragment.tripId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        val view = binding.root

        tripService = TripService(view.context)


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        //tripService.upgrade()

        //Configure button Create Trip
        binding.button2.setOnClickListener {
            val intent = Intent(container!!.context, CreateTripActivity::class.java)
            startActivityForResult(intent, CREATE_TRIP_REQUEST)
            //Log.d("Columns in trip_table", "Number: ${tripService.getNumberOfColumns()}")
        }


        //Configure Spinner Trip Status
        val listTripStatus = arrayOf(
            "ALL",
            TRIP_STATE.PLANNING.toString(),
            TRIP_STATE.STARTED.toString(),
            TRIP_STATE.FINISHED.toString()
        )
        var adapter: ArrayAdapter<String> =
            ArrayAdapter(container!!.context, android.R.layout.simple_spinner_item, listTripStatus)
        binding.spinnerStatus.adapter = adapter
        binding.spinnerStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("statusSelected", binding.spinnerStatus.selectedItem.toString())
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
                        tripAdapter.archiveTrip(viewHolder.adapterPosition, statusSelected)
                    }

                }
            }
        }
        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = tripAdapter
        tripAdapter.onItemClick = {
            val intent = Intent(this.context, TripInformationActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_INFORMATION, it.getId())
            startActivity(intent)
        }
    }

}