package com.example.planventure.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.interfaces_abstracts.SQLiteRepository
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("SimpleDateFormat")
class TripRepository(private val context: Context) : SQLiteRepository<Trip, Int>(context, TRIP_TABLE) {

    override fun addToDB(p: Pair<Trip, Int>): Boolean {

        // put all data into content values
        val cv = buildContentValues(p.first)

        // append all related participants from the ArrayList participants in Trip to the DB
        val participantRepository = ParticipantRepository(context)
        for (p in p.first.getParticipants()) participantRepository.addToDB(Pair(p, p.getId().toInt()))

        // append all related expenses from the ArrayList expenses in Trip to the DB
        val expenseRepository = ExpenseRepository(context)
        for (e in p.first.getExpenses()) expenseRepository.addToDB(Pair(e, e.getId().toInt()))

        // insert the values into the DB and return the result
        // result is either successful of fail
        return when (wdb.insert(TRIP_TABLE, null, cv)) {
            -1L -> false
            else -> true
        }
    }

    /**
     * return the number of columns in the trip table
     * @return number of columns in the trip table
     */
    fun getNumberOfColumns(): Int {
        // Query to count the number of columns
        val queryString = "SELECT COUNT(*) FROM pragma_table_info(\"$TRIP_TABLE\")"

        // execute the query
        val cursor = rdb.rawQuery(queryString, null)

        // if there is a result, get the first value from the result
        val number = if (cursor.moveToFirst()) cursor.getInt(0) else 0

        cursor.close()
        return number
    }

    /**
     * used to get a all objects from the database by their state
     * @param s :current state of the trip
     * @return List of objects mapped from the database via ER mapping
     */
    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip> {
        // Query to get all Trips from the trip table filtered by their state
        val queryString = "SELECT * FROM $TRIP_TABLE WHERE $COLUMN_TRIP_STATE = \"$s\""

        return mapQueryToList(queryString)
    }

    /**
     * There might be a logical error here.
     * But since we use a local DB the error disappears.
     * Used to get a Trip by a participating person
     * @param id : primary key of the participant
     * @return Trip where the participant attended
     */
    fun getTripByParticipantId(id: Int): Trip? {
        val participantRepository = ParticipantRepository(context)
        return getById(participantRepository.getTripIdByParticipantId(id))
    }

    /**
     * sets the state of a specific trip to finished
     * @param id : primary key of the trip which is supposed to finish
     * @return boolean that shows whether operation was successful or not
     */
    fun finishTripById(id: Int): Boolean {
        // Query to update the state of a trip with designated id to new state
        val stringQuery = "UPDATE $TRIP_TABLE SET $COLUMN_TRIP_STATE = 'FINISHED' WHERE ID = $id"

        // execute query
        val cursor = wdb.rawQuery(stringQuery, null)

        // close cursor after operation
        // return whether operation was successful or failed
        return closeAndReturn(cursor)
    }

    override fun buildObjectFromCursor(c: Cursor): Trip {
        val id = c.getInt(0)
        val name = c.getString(1)
        val startDate = parseDateString(c.getString(2))
        val endDate = parseDateString(c.getString(3))
        val location = c.getString(4)
        val number = c.getInt(5)
        val description = c.getString(6)
        val state = c.getString(7)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return Trip(
            id.toLong(), name, formatter.parse(startDate),
            formatter.parse(endDate), location, number, description,
            ArrayList(), ArrayList(),
            when (state) {
                "PLANNING" -> TRIP_STATE.PLANNING
                "STARTED" -> TRIP_STATE.STARTED
                else -> TRIP_STATE.FINISHED
            }
        )
    }

    override fun mapQueryToList(query: String): ArrayList<Trip> {
        val returnList = ArrayList<Trip>()
        val cursor = rdb.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val trip = buildObjectFromCursor(cursor)
                appendParticipants(trip)
                appendExpenses(trip)
                returnList.add(trip)
            } while (cursor.moveToNext())
        } else {
            // failure. Do not add anything to list
        }
        cursor.close()
        return returnList
    }

    override fun buildContentValues(e: Trip): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_TRIP_NAME, e.getName())
        cv.put(COLUMN_TRIP_START_DATE, e.getStartDate().toString())
        cv.put(COLUMN_TRIP_END_DATE, e.getEndDate().toString())
        cv.put(COLUMN_TRIP_LOCATION, e.getLocation())
        cv.put(COLUMN_TRIP_MAX_PARTICIPANTS, e.getMaxNumberOfParticipants())
        cv.put(COLUMN_TRIP_DESCRIPTION, e.getDescription())
        cv.put(COLUMN_TRIP_STATE, e.getState().toString())
        return cv
    }

    /**
     * parse date from format 'EEE MMM dd HH:mm:ss zzz yyyy' to format 'yyyy-MM-dd'
     * @param ds: Date in string format 'EEE MMM dd HH:mm:ss zzz yyyy'
     * @return date in string format 'yyyy-MM-dd'
     */
    private fun parseDateString(ds: String): String {
        val dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val t = ZonedDateTime.parse(ds, dtf)
        return t.format(DateTimeFormatter.ISO_ZONED_DATE_TIME).substring(0, 10)
    }

    private fun appendParticipants(t: Trip) {
        val participantRepository = ParticipantRepository(context)
        val partList = participantRepository.getParticipantsByTrip(t)
        t.setParticipants(partList)
    }

    private fun appendExpenses(trip: Trip) {
        val expenseRepository = ExpenseRepository(context)
        val expList = expenseRepository.getExpensesByTrip(trip)
        trip.setExpenses(expList)
    }

}