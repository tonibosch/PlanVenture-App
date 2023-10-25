package com.example.planventure.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.interfaces.IRepository
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("SimpleDateFormat")
class TripRepository(private val context: Context) : DataBaseHelper(context), IRepository<Trip> {

    /**
     * adds the attributes of a Trip object to the database
     * @param t: Trip object whose attributes are to be stored
     * @return boolean to check whether operation was successful
     */
    fun addTripToDb(t: Trip): Boolean {


        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_TRIP_NAME, t.getName())
        cv.put(COLUMN_TRIP_START_DATE, t.getStartDate().toString())
        cv.put(COLUMN_TRIP_END_DATE, t.getEndDate().toString())
        cv.put(COLUMN_TRIP_LOCATION, t.getLocation())
        cv.put(COLUMN_TRIP_MAX_PARTICIPANTS, t.getMaxNumberOfParticipants())
        cv.put(COLUMN_TRIP_DESCRIPTION, t.getDescription())
        cv.put(COLUMN_TRIP_STATE, t.getState().toString())

        val participantRepository = ParticipantRepository(context)
        for(p in t.getParticipants()) participantRepository.addParticipantToDb(p, t.getId().toInt())

        val expenseRepository = ExpenseRepository(context)
        for(e in t.getExpenses()) expenseRepository.addExpensesToDb(e, t.getId().toInt())

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}


/*
        val db = Firebase.firestore
        val trip = hashMapOf(
            "TRIP_ID" to 3,
            COLUMN_TRIP_NAME to t.getName(),
            COLUMN_TRIP_START_DATE to t.getStartDate().toString(),
            COLUMN_TRIP_END_DATE to t.getEndDate().toString(),
            COLUMN_TRIP_LOCATION to t.getLocation(),
            COLUMN_TRIP_DESCRIPTION to t.getDescription(),
            COLUMN_TRIP_MAX_PARTICIPANTS to t.getMaxNumberOfParticipants(),
            COLUMN_TRIP_STATE to t.getState().toString()
        )
        db.collection("trip")
            .add(trip)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                //return@addOnSuccessListener true
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error adding document", Toast.LENGTH_SHORT).show()
                //return@addOnFailureListener false
            }
        return true*/
    }


    override fun findAll(): ArrayList<Trip>{
        val queryString =
            "SELECT * FROM $TRIP_TABLE"
/*
        val trips = ArrayList<Trip>()

        val db = Firebase.firestore
        db.collection("trip")
            .get()
            .addOnSuccessListener { result ->
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                for (document in result) {
                    Log.d("SELECT * FROM FIREBASE", "${document.id} => ${document.data}")
                    trips.add(Trip(
                        document.data["TRIP_ID"].toString().toLong(),
                        document.data[COLUMN_TRIP_NAME].toString(),
                        formatter.parse(parseDateString(document.data.get(COLUMN_TRIP_START_DATE).toString())),
                        formatter.parse(parseDateString(document.data.get(COLUMN_TRIP_END_DATE).toString())),
                        document.data[COLUMN_TRIP_LOCATION].toString(),
                        document.data[COLUMN_TRIP_MAX_PARTICIPANTS].toString().toInt(),
                        document.data[COLUMN_TRIP_DESCRIPTION].toString(),
                        ArrayList(), ArrayList(),
                        when(document.data[COLUMN_TRIP_STATE]){
                            "OPEN"-> TRIP_STATE.PLANNING
                            "CLOSED" -> TRIP_STATE.STARTED
                            else -> TRIP_STATE.FINISHED
                        }
                    ))
                    Log.d("Trips list", trips.toString())
                }
                Log.d("Trips list", trips.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("SELECT * FROM FIREBASE", "Error getting documents.", exception)
            }

        return trips*/
        return mapQueryToString(queryString)
    }

    fun getSize(): Int{
        val queryString =
            "SELECT COUNT(*) FROM $TRIP_TABLE"

        val number: Int
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        number = if(cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return number
    }

    fun getNumberOfColumns(): Int{
        val queryString =
            "SELECT COUNT(*) FROM pragma_table_info(\"$TRIP_TABLE\")"

        val number: Int
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        number = if(cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return number
    }

    override fun getById(id: Long): Trip? {
        val queryString =
            "SELECT * FROM $TRIP_TABLE WHERE TRIP_ID = $id"
        var trip: Trip? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            trip = buildTripFromCursor(cursor)
            appendParticipants(trip)
            appendExpenses(trip)
        } // else failure
        cursor.close()
        return trip
    }

    /**
     * used to get a specific object from the database by name
     * but do not use this function except for emergencies use getTripById() instead
     * @param name name attribute of the object
     * @return List of objects mapped from the database via ER mapping that have designated name
     */
    fun getTripsByName(name: String): ArrayList<Trip> {
        val queryString =
            "SELECT * FROM $TRIP_TABLE WHERE $COLUMN_TRIP_NAME = \"$name\""
        return mapQueryToString(queryString)
    }

    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip> {
        val queryString =
            "SELECT * FROM $TRIP_TABLE WHERE $COLUMN_TRIP_STATE = \"$s\""
        return mapQueryToString(queryString)
    }


    override fun deleteAll(): Boolean{
        val db = this.writableDatabase
        val query =
            "DELETE FROM $TRIP_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)

    }

    override fun deleteById(id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $TRIP_TABLE WHERE TRIP_ID = $id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    /**
     * used to delete specific objects from a table by name
     * @return boolean that shows whether operation was successful or not
     * but do not use this function except for emergencies use delete TripById() instead
     * @return boolean that shows wether operation was successful or not
     * @param name attribute of the object
     */
    fun deleteTripByName(name: String): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $TRIP_TABLE WHERE $COLUMN_TRIP_NAME = \"$name\""
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    override fun updateById(id: Long, e: Trip): Boolean {
        val db = this.writableDatabase
        /*
        val stringQuery =
            "UPDATE $TRIP_TABLE SET $COLUMN_TRIP_NAME = \"${e.getName()}\", " +
                    "$COLUMN_TRIP_START_DATE = \"${e.getStartDate()}\", " +
                    "$COLUMN_TRIP_END_DATE = \"${e.getEndDate()}\", " +
                    "$COLUMN_TRIP_LOCATION = \"${e.getLocation()}\", " +
                    "$COLUMN_TRIP_DESCRIPTION = \"${e.getDescription()}\", " +
                    "$COLUMN_TRIP_MAX_PARTICIPANTS = ${e.getMaxNumberOfParticipants()}, " +
                    "$COLUMN_TRIP_STATE = \"${e.getState()}\" " +
                    "WHERE TRIP_ID = $id"
        val cursor = db.rawQuery(stringQuery, null)
        */
        val cv = ContentValues()

        cv.put(COLUMN_TRIP_NAME, e.getName())
        cv.put(COLUMN_TRIP_START_DATE, e.getStartDate().toString())
        cv.put(COLUMN_TRIP_END_DATE, e.getEndDate().toString())
        cv.put(COLUMN_TRIP_LOCATION, e.getLocation())
        cv.put(COLUMN_TRIP_MAX_PARTICIPANTS, e.getMaxNumberOfParticipants())
        cv.put(COLUMN_TRIP_DESCRIPTION, e.getDescription())
        cv.put(COLUMN_TRIP_STATE, e.getState().toString())

        return when(db.update(TRIP_TABLE, cv, "TRIP_ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}

    }

    // helper functions
    private fun buildTripFromCursor(c: Cursor): Trip{
        val id = c.getInt(0)
        val name = c.getString(1)
        val startDate = parseDateString(c.getString(2))
        val endDate = parseDateString(c.getString(3))
        val location = c.getString(4)
        val number = c.getInt(5)
        val description = c.getString(6)
        val state = c.getString(7)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return Trip(id.toLong(), name, formatter.parse(startDate),
            formatter.parse(endDate), location, number, description,
            ArrayList(), ArrayList(),
            when(state){
                "PLANNING"-> TRIP_STATE.PLANNING
                "STARTED" -> TRIP_STATE.STARTED
                else -> TRIP_STATE.FINISHED
            })
    }

    private fun buildTripObj(){
        // might not be implemented
    }

    private fun parseDateString(ds: String): String{
        val dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val t = ZonedDateTime.parse(ds, dtf)
        return t.format(DateTimeFormatter.ISO_ZONED_DATE_TIME).substring(0, 10)
    }

    private fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }

    private fun appendParticipants(t: Trip){
        val participantRepository = ParticipantRepository(context)
        val partList = participantRepository.getParticipantsByTrip(t)
        t.setParticipants(partList)
    }

    private fun appendExpenses(trip: Trip){
        val expenseRepository = ExpenseRepository(context)
        val expList = expenseRepository.getExpensesByTrip(trip)
        trip.setExpenses(expList)
    }

    private fun mapQueryToString(query: String): ArrayList<Trip>{
        val returnList = ArrayList<Trip>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do {
                val trip = buildTripFromCursor(cursor)
                appendParticipants(trip)
                appendExpenses(trip)
                returnList.add(trip)
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return returnList
    }

}