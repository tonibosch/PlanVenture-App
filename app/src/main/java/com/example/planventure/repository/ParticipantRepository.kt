package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.interfaces.IRepository
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import kotlin.jvm.Throws

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantRepository(context: Context): DataBaseHelper(context), IRepository<Participant> {

    private val tripRepository = TripRepository(context)
    fun addParticipantToDb(p: Participant, foreignKey: Int): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_NAME, p.getName())
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)

        return when(db.insert(PARTICIPANT_TABLE, null, cv)){-1L -> false else -> true}
    }

    override fun findAll(): ArrayList<Participant>{
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE"
        return mapQueryToList(queryString)
    }

    fun getSize(): Long{
        return "SELECT * FROM $PARTICIPANT_TABLE".length.toLong()
    }

    override fun getById(id: Long): Participant? {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE PARTICIPANT_ID = $id"
        var p: Participant? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            p = buildParticipantFromCursor(cursor)
        } // else failure
        cursor.close()
        return p
    }

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

    fun getParticipantsByName(name: String): ArrayList<Participant> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_NAME = \"$name\""
        return mapQueryToList(queryString)
    }

    fun getTripByParticipantId(id:Int): Trip? {
        try {
        val queryString=
            "SELECT * FROM $PARTICIPANT_TABLE WHERE PARTICIPANT_ID = \"$id\""
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


    override fun deleteAll(): Boolean{
        val db = this.writableDatabase
        val query =
            "DELETE FROM $PARTICIPANT_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)

    }

    override fun deleteById(id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $PARTICIPANT_TABLE WHERE PARTICIPANT_ID = $id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    fun deleteParticipantByName(name: String): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $PARTICIPANT_TABLE WHERE PARTICIPANT_NAME = $name"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    override fun updateById(id: Long, e: Participant): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_NAME, e.getName())

        return when(db.update(PARTICIPANT_TABLE, cv, "PARTICIPANT_ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}

    }

    // helper functions
    private fun buildParticipantFromCursor(c: Cursor): Participant{
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

    private fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }

    private fun mapQueryToList(query: String): ArrayList<Participant> {
        val l: ArrayList<Participant> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do {
                l.add(buildParticipantFromCursor(cursor))
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return l
    }


}