package com.example.planventure.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("SimpleDateFormat")
class TripRepository(context: Context) : DataBaseHelper(context) {

    fun addTripToDB(t: Trip): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_TRIP_NAME, t.getName())
        cv.put(COLUMN_TRIP_START_DATE, t.getStartDate().toString())
        cv.put(COLUMN_TRIP_END_DATE, t.getEndDate().toString())
        cv.put(COLUMN_TRIP_LOCATION, t.getLocation())
        cv.put(COLUMN_TRIP_MAX_PARTICIPANTS, t.getMaxNumberOfParticipants())
        cv.put(COLUMN_TRIP_DESCRIPTION, t.getDescription())
        //TODO("Add missing attributes")

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }


    fun findAllTrips(): List<Trip>{
        val returnList = ArrayList<Trip>()
        val queryString =
            "SELECT * FROM $TRIP_TABLE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            do {
                returnList.add(buildTripFromCursor(cursor))
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return returnList
    }

    fun getTripById(id: Long): Trip? {
        val queryString =
            "SELECT * FROM $TRIP_TABLE WHERE TRIP_ID = $id"
        var trip: Trip? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            trip = buildTripFromCursor(cursor)
        }else{
            // failure
        }
        cursor.close()
        return trip
    }

    fun getTripsByName(name: String): List<Trip> {
        val queryString =
            "SELECT * FROM $TRIP_TABLE WHERE TRIP_ID = $name"
        val returnList = ArrayList<Trip>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            returnList.add(buildTripFromCursor(cursor))
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return returnList
    }


    fun deleteAll(): Boolean{
        val db = this.writableDatabase
        val query =
            "DELETE FROM $TRIP_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)

    }

    fun deleteTripById(id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $TRIP_TABLE WHERE TRIP_ID = $id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    fun deleteTripByName(name: String): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $TRIP_TABLE WHERE $COLUMN_TRIP_NAME = $name"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    // helper functions
    private fun buildTripFromCursor(c: Cursor): Trip{
        val id = c.getInt(0)
        val name = c.getString(1)
        val startDate = c.getString(2)
        val endDate = c.getString(3)
        val location = c.getString(4)
        val number = c.getInt(5)
        val description = c.getString(6)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return Trip(id.toLong(), name, formatter.parse(startDate), formatter.parse(endDate), location, number, description, null, null, TRIP_STATE.CLOSED)
    }

    private fun closeAndReturn(c: Cursor):Boolean{
        return if (c.moveToFirst()){
            c.close()
            true
        }else
            false
    }

}