package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Trip
import com.example.planventure.interfaces_abstracts.IRepository
import com.example.planventure.interfaces_abstracts.SQLiteRepository

/**
 * Expense.kt
 * Repository to store and receive date from the EXPENSE_TABLE in the database
 * Extends SQLiteRepository to access reading and writing functions
 * @property getExpensesByTrip(t : Trip): ArrayList<Expense>
 * @property buildObjectFromCursor(c: Cursor): Expense
 * @property buildContentValues(e: Expense): ContentValues
 *
 * @constructor (context: Context)
 *
 * @see IRepository
 * @see SQLiteRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
class ExpenseRepository(context: Context): SQLiteRepository<Expense, Int>(context, EXPENSE_TABLE) {

    /**
     * returns all expenses that relate to a certain trip
     * @param t :Trip to get the id
     * @return ArrayList of Expenses that belong to a trip
     */
    fun getExpensesByTrip(t : Trip): ArrayList<Expense> {
        /*
         * Query to get all Expenses from the expense table filtered by the trip they belong to
         */
        val query = "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_TRIP_FOREIGN_KEY = ${t.getId()}"

        return read(query)
    }

    /**
     * returns all expenses that relate to a certain trip id
     * @param id :Trip id
     * @return ArrayList of Expenses that belong to a trip's id
     */
    fun getExpensesByTripId(id: Long): ArrayList<Expense> {
        /*
         * Query to get all expenses correlated to a trip
         */
        val query = "SELECT * FROM $EXPENSE_TABLE WHERE $COLUMN_TRIP_FOREIGN_KEY = $id"

        return read(query)
    }

    /**
     * stores whether the person who paid for the expense also participates or not
     * @param b: Boolean that indicates whether person participates or not
     * @param e: accompanying expense
     * @return whether operation succeeded or failed
     */
    fun storePayer(b: Boolean, e: Expense): Boolean{
        /*
         * Query to set the last value of expense table
         */
        val query = "UPDATE $EXPENSE_TABLE SET $COLUMN_EXPENSE_PAYER_PARTICIPATES = ${
            if(b) 1 else 0
        } WHERE ID = ${e.getId()}"

        return execute(query)
    }

    /**
     * returns whether the person who paid for the expense also participates or not
     * @param e: accompanying expense
     * @return whether the person who paid for the expense also participates or not
     */
    fun getPayer(e: Expense): Boolean{
        /*
         * Query to get whether the one who paid for an expense also participated or not
         */
        val query = "SELECT $COLUMN_EXPENSE_PAYER_PARTICIPATES FROM $EXPENSE_TABLE WHERE ID = ${e.getId()}"

        /*
         * execute the query
         */
        val cursor = rdb.rawQuery(query, null)

        /*
         * if there is a result, get the first value from the result
         */
        val number = if (cursor.moveToFirst())
            cursor.getInt(0)
        else
            0

        cursor.close()
        return number == 1
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
        cv.put(COLUMN_EXPENSE_PAYER_PARTICIPATES, 1)
        return cv
    }
}

