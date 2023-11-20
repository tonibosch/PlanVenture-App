package com.example.planventure.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.interfaces_abstracts.IRepository
import com.example.planventure.interfaces_abstracts.Query
import com.example.planventure.interfaces_abstracts.SQLiteRepository
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * TripRepository.kt
 * Repository to store and receive date from the TRIP_TABLE in the database
 * Extends SQLiteRepository to access reading and writing functions
 * @property addToDB(p: Pair<Trip, Int>): Boolean
 * @property getNumberOfColumns(): Int
 * @property getTripsByState(s: TRIP_STATE): ArrayList<Trip>
 * @property getTripByParticipantId(id: Int): Trip?
 * @property finishTripById(id: Int): Boolean
 * @property buildObjectFromCursor(c: Cursor): Trip
 * @property mapQueryToList(query: Query): ArrayList<Trip>
 * @property buildContentValues(e: Trip): ContentValues
 * @property parseDateString(ds: String): String
 * @property appendParticipants(t: Trip)
 * @property appendExpenses(t: Trip)
 *
 * @constructor (context: Context)
 *
 * @see IRepository
 * @see SQLiteRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("SimpleDateFormat")
class TripRepository(private val context: Context) : SQLiteRepository<Trip, Int>(context, TRIP_TABLE) {

    override fun addToDB(p: Pair<Trip, Int>): Boolean {

        /*
         * put all data into content values
         */
        val cv = buildContentValues(p.first)

        /*
         * append all related participants from the ArrayList participants in Trip to the DB
         */
        val participantRepository = ParticipantRepository(context)
        for (participant in p.first.getParticipants())
            participantRepository.addToDB(Pair(participant, participant.getId().toInt()))

        /*
         * append all related expenses from the ArrayList expenses in Trip to the DB
         */
        val expenseRepository = ExpenseRepository(context)
        for (e in p.first.getExpenses())
            expenseRepository.addToDB(Pair(e, e.getId().toInt()))

        /*
         * insert the values into the DB and return the result
         * result is either successful or fail
         */
        return create(cv)
    }

    /**
     * return the number of columns in the trip table
     * @return number of columns in the trip table
     */
    fun getNumberOfColumns(): Int {
        /*
         * Query to count the number of columns
         */
        val query = "SELECT COUNT(*) FROM pragma_table_info(\"$TRIP_TABLE\")"

        /*
         * execute the query
         */
        val cursor = rdb.rawQuery(query, null)

        /*
         * if there is a result, get the first value from the result
         */
        val number = if (cursor.moveToFirst())
            cursor.getInt(0)
        else
            0

        cursor.close()
        return number
    }

    /**
     * used to get a all objects from the database by their state
     * @param s :current state of the trip
     * @return List of objects mapped from the database via ER mapping
     */
    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip> {
        /*
         * Query to get all Trips from the trip table filtered by their state
         */
        val query = "SELECT * FROM $TRIP_TABLE WHERE $COLUMN_TRIP_STATE = \"$s\""

        return read(query)
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
        /*
         * Query to update the state of a trip with designated id to new state
         */
        val string = "UPDATE $TRIP_TABLE SET $COLUMN_TRIP_STATE = 'FINISHED' WHERE ID = $id"
        return execute(string)
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
            /* id of the trip */            id.toLong(),
            /* name of the trip */          name,
            /* start date of the trip */    formatter.parse(startDate),
            /* end date of the trip */      formatter.parse(endDate),
            /* location of the trip */      location,
            /* number of participants */    number,
            /* trip description */          description,
            /* for now empty List for
             * participants */              ArrayList(),
            /* for now empty list for
             * expenses */                  ArrayList(),
            /* state of the trip */         when (state) {
                                                "PLANNING" -> TRIP_STATE.PLANNING
                                                "STARTED" -> TRIP_STATE.STARTED
                                                else -> TRIP_STATE.FINISHED
                                            }
        )
    }

    override fun mapQueryToList(q: Query): ArrayList<Trip> {
        val returnList = ArrayList<Trip>()

        /*
         * execute the query and get the cursor
         */
        val cursor = rdb.rawQuery(q, null)

        /*
         * get cursor values if cursor is not empty
         */
        if (cursor.moveToFirst()) {

            /*
             * while there is data left in the cursor
             */
            do {

                /*
                 * build the trip from the cursor values
                 */
                val trip = buildObjectFromCursor(cursor)
                appendParticipants(trip)
                appendExpenses(trip)
                returnList.add(trip)

            } while (cursor.moveToNext())

        } else {
            /*
             * failure. Do not add anything to list
             */
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
        /*
         * create a formatter of desired format
         */
        val dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        /*
         * put date string and formatter string in ZonedDateTime variable to enable parsing
         */
        val t = ZonedDateTime.parse(ds, dtf)

        /*
         * get the first 10 characters of the resulting string after formatting
         */
        return t.format(DateTimeFormatter.ISO_ZONED_DATE_TIME).substring(0, 10)
    }

    /**
     * used to get all participants related to a trip to add them to the trip specific list
     * @param t :Trip to get the trip id
     */
    private fun appendParticipants(t: Trip) {
        val participantRepository = ParticipantRepository(context)

        /*
         * get list of participants related to a trip
         */
        val partList = participantRepository.getParticipantsByTrip(t)

        /*
         * set the trips participant list to the new one
         */
        t.setParticipants(partList)
    }

    /**
     * used to get all expenses related to a trip to add them to the trip specific list
     * @param t :Trip to get the trip id
     */
    private fun appendExpenses(t: Trip) {
        val expenseRepository = ExpenseRepository(context)

        /*
         * get list of expenses related to a trip
         */
        val expList = expenseRepository.getExpensesByTrip(t)

        /*
         * set the trips expenses list to the new one
         */
        t.setExpenses(expList)
    }

}