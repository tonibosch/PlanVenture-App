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

    private val tripRepository = TripRepository(applicationContext)

    fun addTrip(trip: Trip){

        val success = tripRepository.addTripToDb(trip)
        Log.d("TRIP DB INPUT", success.toString())
    }

    fun getAllTrips(): List<Trip>{
        return tripRepository.findAll()
    }

    fun getTripById(id: Long): Trip?{
        return tripRepository.getById(id)
    }

    fun getTripsByName(name: String): List<Trip>{
        return tripRepository.getTripsByName(name)
    }

    fun deleteTripById(id: Long){
        val success = tripRepository.deleteById(id.toInt())
    }

    fun deleteTripsByName(name: String){
        val success = tripRepository.deleteTripByName(name)
    }

    fun deleteAllTrips(){
        val success = tripRepository.deleteAll()
    }

}