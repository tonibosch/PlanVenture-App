package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.Telephony.Mms.Part
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.database.PVRepository
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantRepository(context: Context): DataBaseHelper(context), PVRepository<Participant> {

    fun addParticipantToDb(p: Participant, foreignKey: Int): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_NAME, p.getName())
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    override fun findAll(): ArrayList<Participant>{
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE"
        return mapQueryToList(queryString)
    }

    override fun getById(id: Long): Participant? {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE PARTICIPANT_ID = $id"
        var p: Participant? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            p = buildParticipantFromCursor(cursor)
        }else{
            // failure
        }
        cursor.close()
        return p
    }

    fun getParticipantsByTrip(t : Trip): ArrayList<Participant> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    fun getParticipantsByName(name: String): ArrayList<Participant> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_NAME = $name"
        return mapQueryToList(queryString)
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
            "DELETE FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_NAME = $name"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    fun alterParticipantById(id: Int, p: Participant, t: Trip){
        deleteById(id)
        addParticipantToDb(p, t.getId().toInt())
    }

    fun alterParticipantByName(name: String, p: Participant, t: Trip){
        deleteParticipantByName("name")
        addParticipantToDb(p, t.getId().toInt())
    }

    // helper functions
    private fun buildParticipantFromCursor(c: Cursor): Participant{
        val name = c.getString(1)

        return Participant(name)
    }

    private fun closeAndReturn(c: Cursor):Boolean{
        return if (c.moveToFirst()){
            c.close()
            true
        }else
            false
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