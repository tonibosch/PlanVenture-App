package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Trip
import com.example.planventure.interfaces_abstracts.SQLiteRepository

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseRepository(private val context: Context): SQLiteRepository<Expense>(context, EXPENSE_TABLE) {


    fun addExpensesToDb(e: Expense, foreignKey: Int): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_NAME, e.getName())
        cv.put(COLUMN_EXPENSE_AMOUNT, e.getAmount())
        cv.put(COLUMN_PARTICIPANT_TRIP, foreignKey)

        return when(db.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }


    fun getExpensesByTrip(t : Trip): ArrayList<Expense> {
        val queryString =
            "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    override fun updateById(id: Long, e: Expense): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_NAME, e.getName())
        cv.put(COLUMN_EXPENSE_AMOUNT, e.getAmount())

        return when(db.update(TRIP_TABLE, cv, "ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}
    }

    // helper functions

    override fun buildObjectFromCursor(c: Cursor): Expense {
        val id = c.getInt(0)
        val name = c.getString(1)
        val amount = c.getFloat(2)

        return Expense(id.toLong(), name, amount)
    }



    override fun mapQueryToList(query: String): ArrayList<Expense>{
        val l: ArrayList<Expense> = ArrayList()
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

