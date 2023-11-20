package com.example.planventure.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.repository.ParticipantRepository
import com.example.planventure.repository.TripRepository
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class TripService(
    private val applicationContext: Context
) {

    private val tripRepository = TripRepository(applicationContext)
    private val participantRepository = ParticipantRepository(applicationContext)

    /**
     * creates Trip object and propagates Error to the Activity
     */
    fun addTrip(myData: MutableList<String>) {
        try {
            checkEmptyDates(myData[1])
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            checkMaxParticipants(myData[4])
            val trip = Trip(-1,
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
            checksNullValues(trip)

            Log.d("TRIP STATE", trip.getState().toString())
            val success = tripRepository.addToDB(Pair(trip, -1))
            Log.d("TRIP DB INPUT", success.toString())
        } catch (e: EmptyDataException) {
            throw e
        }
    }


    fun getAllTrips(): List<Trip> {
        return tripRepository.findAll()
    }

    fun getTripById(id: Long): Trip? {
        Log.d("TRIPID",id.toString())
        return tripRepository.getById(id)
    }

    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip> {
        return tripRepository.getTripsByState(s)
    }

    fun updateTrip(id: Long, t: Trip) {
        val success = tripRepository.updateById(id, t)
        Log.d("UPDATE_TRIP", success.toString())
    }

    fun deleteTripById(id: Long) {
        val success = tripRepository.deleteById(id.toInt())
    }

    fun deleteAllTrips() {
        val success = tripRepository.deleteAll()
    }

    fun finishTripById(id:Long) {
        val success = tripRepository.finishTripById(id.toInt())
    }

    fun getNumberOfColumns(): Int{
        return tripRepository.getNumberOfColumns()
    }


    /**
     * checks for Null values and throws EmptyDataException
     */
    private fun checksNullValues(trip: Trip) {
        if (trip.getName().isEmpty()) {
            throw EmptyDataException("Enter a trip name")
        } else if (trip.getLocation().isEmpty()) {
            throw EmptyDataException("Enter a destination")
        }
    }

    private fun checkEmptyDates(string:String){
        if (string == "Select start date")
            throw EmptyDataException("Please select a date")
    }

    private fun checkMaxParticipants(maxParticipants: String) {
        if(maxParticipants.isEmpty()){
            throw EmptyDataException("Please enter a number of participants")
        } else if (maxParticipants.toInt() > 20) {
            throw MaxParticipantsOverflow("A trip cannot have more than 20 participants!")
        } else if(maxParticipants.toInt() <= 1){
            throw Exception("A trip cannot have less than 2 Participants")
        }
    }

}