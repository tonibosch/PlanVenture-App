package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.entity.Participant

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantParticipantRepository(context: Context): DataBaseHelper(context){

    private val tripRepository = TripRepository(context)
    fun addParticipantsAndAmountToDb(p1: Participant, p2: Participant, amount: Float): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_1_ID, p1.getId())
        cv.put(COLUMN_PARTICIPANT_2_ID, p2.getId())
        cv.put(COLUMN_PAID_AMOUNT, amount)

        return when(db.insert(PARTICIPANT_PARTICIPANT_TABLE, null, cv)){-1L -> false else -> true}
    }

    fun findAll(): List<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_PARTICIPANT_TABLE#"
        return mapQueryToList(queryString)
    }

    fun getByParticipant1And2Id(participant_1_id: Long, participant_2_id: Long): ArrayList<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM ${PARTICIPANT_PARTICIPANT_TABLE}_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_1_id AND $COLUMN_EXPENSE_ID = $participant_2_id"
        return mapQueryToList(queryString)
    }


    fun deleteAll(): Boolean {
        val db = this.writableDatabase
        val query =
            "DELETE FROM $PARTICIPANT_PARTICIPANT_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)
    }

    fun deleteByParticipant1and2Id(participant_1_id: Long, participant_2_id: Long): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $PARTICIPANT_PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_1_id AND $COLUMN_EXPENSE_ID = $participant_2_id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }


    private fun mapQueryToList(query: String): ArrayList<Triple<Int, Int, Float>>{
        val l: ArrayList<Triple<Int, Int, Float>> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do {
                l.add(buildExpenseFromCursor(cursor))
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return l
    }

    private fun buildExpenseFromCursor(c: Cursor): Triple<Int, Int, Float> {
        val p_id = c.getInt(0)
        val e_id = c.getInt(1)
        val amount = c.getFloat(2)

        return Triple(p_id, e_id, amount)
    }

    private fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }

}