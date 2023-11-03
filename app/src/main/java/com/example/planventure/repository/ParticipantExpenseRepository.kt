package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.interfaces_abstracts.SQLiteRepository

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantExpenseRepository(private val context: Context):
    SQLiteRepository<Triple<Int, Int, Float>, Int>(context, PARTICIPANT_EXPENSE_TABLE) {

    override fun addToDB(p: Pair<Triple<Int, Int, Float>, Int>): Boolean{
        val cv = buildContentValues(p.first)
        return when(wdb.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    override fun findAll(): ArrayList<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_EXPENSE_TABLE"
        return mapQueryToList(queryString)
    }

    override fun getById(id: Long): Triple<Int, Int, Float>? {
        //
        return null
    }

    override fun updateById(id: Long, e: Triple<Int, Int, Float>): Boolean {
        //
        return false
    }

    override fun deleteAll(): Boolean {
        val db = this.writableDatabase
        val query =
            "DELETE FROM $PARTICIPANT_EXPENSE_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)
    }

    override fun deleteById(id: Int): Boolean {
        //
        return false
    }

    fun getByParticipantExpenseId(participant_id: Long, expense_id: Long): ArrayList<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_id AND $COLUMN_EXPENSE_ID = $expense_id"
        return mapQueryToList(queryString)
    }


    fun deleteByExpenseParticipantId(expense_id: Int, participant_id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_id AND $COLUMN_EXPENSE_ID = $expense_id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }


    override fun mapQueryToList(query: String): ArrayList<Triple<Int, Int, Float>>{
        val l: ArrayList<Triple<Int, Int, Float>> = ArrayList()
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

    override fun buildContentValues(e: Triple<Int, Int, Float>): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_PARTICIPANT_ID, e.first)
        cv.put(COLUMN_EXPENSE_ID, e.second)
        cv.put(COLUMN_SPENT_AMOUNT, e.third)
        return cv
    }

    override fun buildObjectFromCursor(c: Cursor): Triple<Int, Int, Float> {
        val p_id = c.getInt(0)
        val e_id = c.getInt(1)
        val amount = c.getFloat(2)

        return Triple(p_id, e_id, amount)
    }

}