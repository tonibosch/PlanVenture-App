package com.example.planventure.service

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Trip
import com.example.planventure.repository.TripRepository

@RequiresApi(Build.VERSION_CODES.P)
class TripService(
    private val applicationContext: Context
) {


    fun addTrip(trip: Trip){
        val tripRepository = TripRepository(applicationContext)
        val success = tripRepository.addTripToDb(trip)
        Log.d("TRIP DB INPUT", success.toString())
    }

    fun getAllTrips(): List<Trip>{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.findAll()
    }

    fun getTripById(id: Long): Trip?{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.getById(id)
    }

    fun getTripsByName(name: String): List<Trip>{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.getTripsByName(name)
    }

    fun deleteTripById(id: Long){
        val tripRepository = TripRepository(applicationContext)
        val success = tripRepository.deleteById(id.toInt())
    }

    fun deleteTripsByName(name: String){
        val tripRepository = TripRepository(applicationContext)
        val success = tripRepository.deleteTripByName(name)
    }

    fun deleteAllTrips(){
        val tripRepository = TripRepository(applicationContext)
        val success = tripRepository.deleteAll()
    }

}