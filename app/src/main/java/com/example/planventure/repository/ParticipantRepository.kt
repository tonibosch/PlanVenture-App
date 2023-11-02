package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.interfaces_abstracts.SQLiteRepository

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantRepository(context: Context): SQLiteRepository<Participant>(context, PARTICIPANT_TABLE) {

    private val tripRepository = TripRepository(context)
    fun addParticipantToDb(p: Participant, foreignKey: Int): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_NAME, p.getName())
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)

        return when(db.insert(PARTICIPANT_TABLE, null, cv)){-1L -> false else -> true}
    }


    /**
     * be careful with this function since it returns a List due to the fact that name is no primary key
     */
    fun getParticipantsByTrip(t : Trip): ArrayList<Participant> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    fun getParticipantsByTripId(id:Long): ArrayList<Participant> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_TRIP = $id"
        return mapQueryToList(queryString)
    }


    fun getTripByParticipantId(id:Int): Trip? {
        try {
        val queryString=
            "SELECT * FROM $PARTICIPANT_TABLE WHERE ID = \"$id\""
        var t:Trip? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString,null)
        if(cursor.moveToFirst()){
            t = buildTripFromCursor(cursor)
        } // else failure
        cursor.close()
        return t
        } catch (e:Error){
            throw e
        }
    }


    override fun updateById(id: Long, e: Participant): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_NAME, e.getName())

        return when(db.update(PARTICIPANT_TABLE, cv, "ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}

    }

    // helper functions
    override fun buildObjectFromCursor(c: Cursor): Participant{
        val id = c.getInt(0)
        val name = c.getString(1)
        return Participant(id.toLong(), name)
    }

    private fun buildTripFromCursor(c: Cursor): Trip {
        val trip = tripRepository.getById(c.getInt(2).toLong())

        if(trip != null)
         return trip
        else{
            throw Error("Trip can not be null")
        }
    }

    override fun mapQueryToList(query: String): ArrayList<Participant> {
        val l: ArrayList<Participant> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do {
                l.add(buildObjectFromCursor(cursor))
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return l
    }

}