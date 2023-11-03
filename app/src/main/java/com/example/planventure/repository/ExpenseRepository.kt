package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.interfaces_abstracts.SQLiteRepository

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseRepository(private val context: Context): SQLiteRepository<Expense, Int>(context, EXPENSE_TABLE) {

    override fun addToDB(p: Pair<Expense, Int>): Boolean{
        val cv = buildContentValues(p.first)
        cv.put(COLUMN_PARTICIPANT_TRIP, p.second)
        return when(wdb.insert(TRIP_TABLE, null, cv)){-1L -> false else -> true}
    }

    fun getExpensesByTrip(t : Trip): ArrayList<Expense> {
        val queryString = "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_TRIP = ${t.getId()}"
        return mapQueryToList(queryString)
    }

    override fun buildObjectFromCursor(c: Cursor): Expense {
        val id = c.getInt(0)
        val name = c.getString(1)
        val amount = c.getFloat(2)
        return Expense(id.toLong(), name, amount)
    }

    override fun mapQueryToList(query: String): ArrayList<Expense>{
        val l: ArrayList<Expense> = ArrayList()
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
    override fun buildContentValues(e: Expense): ContentValues {
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_NAME, e.getName())
        cv.put(COLUMN_EXPENSE_AMOUNT, e.getAmount())
        return cv
    }
}

