package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.interfaces.IRepository

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantExpenseRepository(private val context: Context): DataBaseHelper(context)/*, IRepository<Triple<Int, Int, Float>>*/{

    fun addExpenseAndParticipantToDb(e: Expense, p: Participant, amount: Float): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_ID, p.getName())
        cv.put(COLUMN_EXPENSE_ID, e.getName())
        cv.put(COLUMN_SPENT_AMOUNT, amount)

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    fun findAll(): List<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_EXPENSE_TABLE"
        return mapQueryToList(queryString)
    }

    fun getByParticipantExpenseId(participant_id: Long, expense_id: Long): ArrayList<Triple<Int, Int, Float>> {
        val queryString =
            "SELECT * FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_id AND $COLUMN_EXPENSE_ID = $expense_id"
        return mapQueryToList(queryString)
    }

    fun deleteAll(): Boolean {
        val db = this.writableDatabase
        val query =
            "DELETE FROM $PARTICIPANT_EXPENSE_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)
    }

    fun deleteByExpenseParticipantId(expense_id: Int, participant_id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_PARTICIPANT_ID = $participant_id AND $COLUMN_EXPENSE_ID = $expense_id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }



    /*
    fun updateById(e: Expense, p: Participant, amount: Float): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_PARTICIPANT_ID, p.getId())
        cv.put(COLUMN_EXPENSE_ID, e.getId())
        cv.put(COLUMN_SPENT_AMOUNT, amount)

        return when(db.update(TRIP_TABLE, cv, "PARTICIPANT_ID=? AND EXPENSE_ID=?", arrayOf(p.getId().toString()))) {-1 -> false else -> true}
    }*/


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