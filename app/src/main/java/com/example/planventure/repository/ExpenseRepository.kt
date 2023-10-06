package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.opengl.ETC1
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper
import com.example.planventure.database.PVRepository
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseRepository(private val context: Context): DataBaseHelper(context), PVRepository<Expense> {

    fun addExpensesToDb(e: Expense, foreignKey: Int): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_NAME, e.getName())
        cv.put(COLUMN_EXPENSE_AMOUNT, e.getAmount())
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    override fun findAll(): ArrayList<Expense>{
        val queryString =
            "SELECT * FROM $EXPENSE_TABLE"
        return mapQueryToList(queryString)
    }

    override fun getById(id: Long): Expense? {
        val queryString =
            "SELECT * FROM $EXPENSE_TABLE WHERE EXPENSE_ID = $id"
        var e: Expense? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            e = buildExpenseFromCursor(cursor)
        }else{
            // failure
        }
        cursor.close()
        return e
    }

    fun getExpensesByTrip(t : Trip): ArrayList<Expense> {
        val queryString =
            "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    fun getParticipantsByName(name: String): ArrayList<Expense> {
        val queryString =
            "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_NAME = $name"
        return mapQueryToList(queryString)
    }

    override fun deleteAll(): Boolean{
        val db = this.writableDatabase
        val query =
            "DELETE FROM $EXPENSE_TABLE"
        val cursor = db.rawQuery(query, null)
        return closeAndReturn(cursor)

    }

    override fun deleteById(id: Int): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $EXPENSE_TABLE WHERE EXPENSE_ID = $id"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    fun deleteExpenseByName(name: String): Boolean {
        val db = this.writableDatabase
        val stringQuery =
            "DELETE FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_NAME = $name"
        val cursor = db.rawQuery(stringQuery, null)
        return closeAndReturn(cursor)
    }

    fun alterExpenseById(id: Int, e: Expense, t: Trip){
        deleteById(id)
        addExpensesToDb(e, t.getId().toInt())
    }

    fun alterExpenseByName(name: String, e: Expense, t: Trip){
        deleteExpenseByName("name")
        addExpensesToDb(e, t.getId().toInt())
    }

    // helper functions
    private fun buildExpenseFromCursor(c: Cursor): Expense {
        // 0 is id
        val name = c.getString(1)
        val amount = c.getFloat(2)

        return Expense(name, amount)
    }

    private fun closeAndReturn(c: Cursor):Boolean{
        //return if (c.moveToFirst()){
          //  c.close()
            //true
        //}else
          //  false
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }

    private fun mapQueryToList(query: String): ArrayList<Expense>{
        val l: ArrayList<Expense> = ArrayList()
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
}

