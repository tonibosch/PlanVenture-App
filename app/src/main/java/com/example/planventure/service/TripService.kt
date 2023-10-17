package com.example.planventure.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.repository.TripRepository
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class TripService(
    private val applicationContext: Context
) {

    private val tripRepository = TripRepository(applicationContext)

    //creates Trip object and propagates Error to the Activity
    fun addTrip(myData: MutableList<String>) {
        try {

            checksNullValues(myData)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val trip = Trip(
                tripRepository.getSize().toLong() + 1,
                myData[0],
                formatter.parse(myData[1]),
                formatter.parse(myData[2]),
                myData[3],
                myData[4].toInt(),
                myData[5],
                ArrayList(),
                ArrayList(),
                TRIP_STATE.PLANNING
            )
            val success = tripRepository.addTripToDb(trip)
            Log.d("TRIP DB INPUT", success.toString())
        } catch (e: EmptyDataException) {
            throw e
        }
    }

    fun getAllTrips(): List<Trip> {
        return tripRepository.findAll()
    }

    fun getTripById(id: Long): Trip? {
        return tripRepository.getById(id)
    }

    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip>{
        return tripRepository.getTripsByState(s)
    }

    fun updateTrip(id: Long, t: Trip){
        val success = tripRepository.updateById(id, t)
        Log.d("UPDATE_TRIP", success.toString())
    }

    /**
     * use it carefully, since it returns a list due to the fact that the name is no primary key and hence not unique
     */
    fun getTripsByName(name: String): List<Trip> {
        return tripRepository.getTripsByName(name)
    }

    fun deleteTripById(id: Long) {
        val success = tripRepository.deleteById(id.toInt())
    }

    fun deleteTripsByName(name: String) {
        val success = tripRepository.deleteTripByName(name)
    }

    fun deleteAllTrips() {
        val success = tripRepository.deleteAll()
    }

    fun getNumberOfColumns(): Int{
        return tripRepository.getNumberOfColumns()
    }


    //checks for Null values and throws EmptyDataException
    private fun checksNullValues(myData: MutableList<String>){
        if (myData[0].isEmpty()) {
            throw EmptyDataException("Enter a trip name")
        } else if (myData[1] == "Select start date") {
            throw EmptyDataException("Select dates for the trip")
        } else if (myData[3].isEmpty()) {
            throw EmptyDataException("Enter a destination")
        } else if (myData[4].isEmpty()) {
            throw EmptyDataException("Enter the number of participants")
        }
    }

}