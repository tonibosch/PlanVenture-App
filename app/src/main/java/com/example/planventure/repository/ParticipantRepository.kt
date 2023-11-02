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

    fun addParticipantToDb(p: Participant, foreignKey: Int): Boolean{
        val cv = buildContentValues(p)
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)
        return when(wdb.insert(PARTICIPANT_TABLE, null, cv)){-1L -> false else -> true}
    }

    fun getParticipantsByTrip(t : Trip): ArrayList<Participant> {
        val queryString = "SELECT * FROM $PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    fun getTripIdByParticipantId(id:Int): Long {
        val queryString= "SELECT $COLUMN_PARTICIPANT_TRIP FROM $PARTICIPANT_TABLE WHERE ID = $id"
        var id = 0L;
        val cursor = rdb.rawQuery(queryString,null)
        if(cursor.moveToFirst()){
            id = cursor.getInt(0).toLong()
        } // else failure
        cursor.close()
        return id
    }

    override fun buildObjectFromCursor(c: Cursor): Participant{
        val id = c.getInt(0)
        val name = c.getString(1)
        return Participant(id.toLong(), name)
    }

    override fun mapQueryToList(query: String): ArrayList<Participant> {
        val l: ArrayList<Participant> = ArrayList()
        val cursor = rdb.rawQuery(query, null)
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

    override fun buildContentValues(e: Participant): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_PARTICIPANT_NAME, e.getName())
        return cv
    }

}