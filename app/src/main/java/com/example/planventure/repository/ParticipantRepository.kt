package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.interfaces_abstracts.IRepository
import com.example.planventure.interfaces_abstracts.SQLiteRepository

/**
 * ParticipantRepository.kt
 * Repository to store and receive date from the PARTICIPANT_TABLE in the database
 * Extends SQLiteRepository to access reading and writing functions
 * @property getParticipantsByTrip(t : Trip): ArrayList<Participant>
 * @property getTripIdByParticipantId(id: Int): Long
 * @property buildObjectFromCursor(c: Cursor): Participant
 * @property buildContentValues(e: Participant): ContentValues
 *
 * @constructor (context: Context)
 *
 * @see IRepository
 * @see SQLiteRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
class ParticipantRepository(context: Context): SQLiteRepository<Participant, Int>(context, PARTICIPANT_TABLE) {

    /**
     * returns all participants that attend on a certain trip
     * @param t :Trip to get the id
     * @return ArrayList of Participants that belong to a trip
     */
    fun getParticipantsByTrip(t : Trip): ArrayList<Participant> {
        /*
         * Query to get all Participants from the participant table filtered by the trip they belong to
         */
        val query = "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_TRIP_FOREIGN_KEY = ${t.getId()}"

        return read(query)
    }

    /**
     * returns the id of a trip related to the participant with the inserted participant id
     * @param id :Int id of the participant whose trip id is to be extracted
     * @return id of the desired trip
     */
    fun getTripIdByParticipantId(id: Int): Long {
        /*
         * Query to get the trip id of a participant id
         */
        val query = "SELECT $COLUMN_TRIP_FOREIGN_KEY FROM $PARTICIPANT_TABLE WHERE ID = $id"

        /*
         * execute Query and store result in cursor
         */
        val cursor = rdb.rawQuery(query,null)

        /*
         * if the cursor contains data extract the data,  store and return it
         * else store and return 0
         */
        val tid = if(cursor.moveToFirst()) cursor.getInt(0).toLong() else 0
        cursor.close()
        return tid
    }

    override fun buildObjectFromCursor(c: Cursor): Participant{
        val id = c.getInt(0)
        val name = c.getString(1)
        return Participant(
            /* participant id */    id.toLong(),
            /* participant name */  name
        )
    }

    override fun buildContentValues(e: Participant): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_PARTICIPANT_NAME, e.getName())
        return cv
    }

}