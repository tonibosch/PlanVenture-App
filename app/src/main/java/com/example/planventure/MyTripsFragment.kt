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

/**
 * A fragment to display a list of trips with filtering options.
 *
 * This fragment is responsible for displaying a list of trips and allows users to filter
 * trips based on their status. It also provides options to create new trips and view trip
 * information.
 *
 * @property binding The binding object for the fragment's layout.
 * @property trips A list of trips to display.
 * @property tripService The service responsible for managing trips.
 * @property tripAdapter The adapter for displaying trips in a RecyclerView.
 */
@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {

    private lateinit var binding: FragmentMyTripsBinding
    private var trips: ArrayList<Trip> = ArrayList()
    //Services
    private lateinit var tripService: TripService
    //Adapters
    private lateinit var tripAdapter: TripAdapter

    companion object {
        private const val CREATE_TRIP_REQUEST = 1 // You can choose any integer value
        const val TRIP_ID_TRIP_INFORMATION = "com.example.planventure.MyTripsFragment.tripId"
    }

    /**
     * Initializes the fragment's view and sets up UI components.
     *
     * @param inflater The layout inflater to inflate the fragment's layout.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state, if any.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        val view = binding.root

        tripService = TripService(view.context)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        //tripService.upgrade()

        //Configure the button to navigate to the screen to create a new trip.
        binding.button2.setOnClickListener {
            val intent = Intent(container!!.context, CreateTripActivity::class.java)
            startActivityForResult(intent, CREATE_TRIP_REQUEST)
        }

        //Configure Spinner Trip Status
        val listTripStatus = arrayOf(
            "ALL",
            TRIP_STATE.PLANNING.toString(),
            TRIP_STATE.STARTED.toString(),
            TRIP_STATE.FINISHED.toString()
        )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(container!!.context, android.R.layout.simple_spinner_item, listTripStatus)
        binding.spinnerStatus.adapter = adapter
        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                refreshTripList()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        refreshTripList()
        return view
    }

    /**
     * Handles the result of an activity launched for creating a new trip.
     *
     * @param requestCode The request code for the activity result.
     * @param resultCode The result code of the activity.
     * @param data The data returned by the activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_TRIP_REQUEST && resultCode == Activity.RESULT_OK) {
            refreshTripList()           //When a new trip has been created, refresh the list of trips
        }
    }
    /**
     * Refreshes the list of trips based on the selected trip status filter.
     */
    private fun refreshTripList() {
        trips = when (binding.spinnerStatus.selectedItem.toString()) {
            TRIP_STATE.PLANNING.toString() -> tripService.getTripsByState(TRIP_STATE.PLANNING)
            TRIP_STATE.STARTED.toString() -> tripService.getTripsByState(TRIP_STATE.STARTED)
            TRIP_STATE.FINISHED.toString() -> tripService.getTripsByState(TRIP_STATE.FINISHED)
            else -> tripService.getAllTrips() as ArrayList<Trip>
        }

        tripAdapter = TripAdapter(trips, this.requireContext())

        //Configure the swipe actions
        val swipeGesture = object : SwipeGesture(this.requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        tripAdapter.deleteTrip(viewHolder.adapterPosition)
                    }
                    ItemTouchHelper.RIGHT -> {
                        tripAdapter.archiveTrip(viewHolder.adapterPosition, binding.spinnerStatus.selectedItem.toString())
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = tripAdapter
        tripAdapter.onItemClick = {
            val intent = Intent(this.context, TripInformationActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_INFORMATION, it.getId())
            startActivity(intent)
        }
    }

}