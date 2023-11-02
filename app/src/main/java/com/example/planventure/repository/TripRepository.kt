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
class TripRepository(private val context: Context) : SQLiteRepository<Trip>(context, TRIP_TABLE) {

    /**
     * adds the attributes of a Trip object to the database
     * @param t: Trip object whose attributes are to be stored
     * @return boolean to check whether operation was successful
     */
    fun addTripToDb(t: Trip): Boolean {

        val cv = buildContentValues(t)

        val participantRepository = ParticipantRepository(context)
        for(p in t.getParticipants()) participantRepository.addParticipantToDb(p, t.getId().toInt())

        val expenseRepository = ExpenseRepository(context)
        for(e in t.getExpenses()) expenseRepository.addExpensesToDb(e, t.getId().toInt())

        return when(wdb.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    fun getNumberOfColumns(): Int{
        val queryString = "SELECT COUNT(*) FROM pragma_table_info(\"$TRIP_TABLE\")"
        val number: Int
        val cursor = rdb.rawQuery(queryString, null)
        number = if(cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return number
    }

    fun getTripsByState(s: TRIP_STATE): ArrayList<Trip> {
        val queryString = "SELECT * FROM $TRIP_TABLE WHERE $COLUMN_TRIP_STATE = \"$s\""
        return mapQueryToList(queryString)
    }

    fun getTripByParticipantId(id:Int): Trip? {
        val participantRepository = ParticipantRepository(context)
        return getById(participantRepository.getTripIdByParticipantId(id))
    }

    fun finishTripById(id:Int): Boolean {
        val stringQuery = "UPDATE $TRIP_TABLE SET $COLUMN_TRIP_STATE = 'FINISHED' WHERE ID = $id"
        val cursor = wdb.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    override fun buildObjectFromCursor(c: Cursor): Trip{
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

    override fun mapQueryToList(query: String): ArrayList<Trip>{
        val returnList = ArrayList<Trip>()
        val cursor = rdb.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do {
                val trip = buildObjectFromCursor(cursor)
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

    private fun parseDateString(ds: String): String{
        val dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val t = ZonedDateTime.parse(ds, dtf)
        return t.format(DateTimeFormatter.ISO_ZONED_DATE_TIME).substring(0, 10)
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

}