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
class ExpenseRepository(context: Context): SQLiteRepository<Expense, Int>(context, EXPENSE_TABLE) {

    /**
     * returns all expenses that relate to a certain trip
     * @param t :Trip to get the id
     * @return ArrayList of Expenses that belong to a trip
     */
    fun getExpensesByTrip(t : Trip): ArrayList<Expense> {
        // Query to get all Expenses from the expense table filtered by the trip they belong to
        val queryString = "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_EXPENSE_TRIP = ${t.getId()}"

        return read(queryString)
    }

    override fun buildObjectFromCursor(c: Cursor): Expense {
        val id = c.getInt(0)
        val name = c.getString(1)
        val amount = c.getFloat(2)
        return Expense(
            /* trip id */       id.toLong(),
            /* trip name */     name,
            /* spent amount */  amount
        )
    }

    override fun buildContentValues(e: Expense): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_EXPENSE_NAME, e.getName())
        cv.put(COLUMN_EXPENSE_AMOUNT, e.getAmount())
        return cv
    }
}

