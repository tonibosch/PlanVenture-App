package com.example.planventure.service

import android.content.Context
import android.os.Build
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
        val success = tripRepository.addTripToDB(trip)
    }

    fun getAllTrips(): List<Trip>{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.findAllTrips()
    }

    fun getTripById(id: Long): Trip?{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.getTripById(id)
    }

    fun getTripsByName(name: String): List<Trip>{
        val tripRepository = TripRepository(applicationContext)
        return tripRepository.getTripsByName(name)
    }

    fun deleteTripById(id: Long){
        val tripRepository = TripRepository(applicationContext)
        val success = tripRepository.deleteTripById(id.toInt())
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