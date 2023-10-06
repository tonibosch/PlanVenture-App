package com.example.planventure

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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

@RequiresApi(Build.VERSION_CODES.P)
class MyTripsFragment : Fragment() {


    private lateinit var button: Button
    private lateinit var scrollLayout: LinearLayout

    //services
    private lateinit var tripService: TripService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_my_trips, container, false)
        tripService = TripService(view.context)

        if (container != null) {


            button = view.findViewById<Button>(R.id.button2)

            val trips = tripService.getAllTrips()
            for(t in trips) Log.d("TRIP", t.toString())

            //button = container.findViewById(R.id.button2)
            button.setOnClickListener {
                Log.d("TEST", "Hallo")
                val intent = Intent(container.context, CreateTripActivity::class.java)
                startActivity(intent)
            }
        }




        return view
    }

}