package com.example.planventure.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
open class DataBaseHelper(
    context: Context
) : SQLiteOpenHelper(context, "planventure.db", null, 1) {

    companion object {
        const val TRIP_TABLE: String = "TRIP_TABLE"
        const val COLUMN_TRIP_NAME: String = "TRIP_NAME"
        const val COLUMN_TRIP_START_DATE: String = "TRIP_START_DATE"
        const val COLUMN_TRIP_END_DATE: String = "TRIP_END_DATE"
        const val COLUMN_TRIP_LOCATION: String = "TRIP_LOCATION"
        const val COLUMN_TRIP_MAX_PARTICIPANTS: String = "TRIP_MAX_PARTICIPANTS"
        const val COLUMN_TRIP_DESCRIPTION: String = "TRIP_DESCRIPTION"
        const val COLUMN_TRIP_STATE: String = "TRIP_STATE"

        const val PARTICIPANT_TABLE: String = "PARTICIPANT_TABLE"
        const val COLUMN_PARTICIPANT_NAME: String = "PARTICIPANT_NAME"
        const val COLUMN_PARTICIPANT_TRIP: String = "PARTICIPANT_TRIP"

        const val EXPENSE_TABLE: String = "EXPENSE_TABLE"
        const val COLUMN_EXPENSE_NAME: String = "EXPENSE_NAME"
        const val COLUMN_EXPENSE_AMOUNT: String = "EXPENSE_AMOUNT"
        const val COLUMN_EXPENSE_TRIP: String = "EXPENSE_TRIP"
    }

    /**
     * called the first time a database is accessed. There should be code in here to create a new database
     * @param db: SQLiteDatabase to execute SQL queries
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTripTableStatement =
            "CREATE TABLE $TRIP_TABLE (TRIP_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TRIP_NAME TEXT, " +
                    "$COLUMN_TRIP_START_DATE TEXT, $COLUMN_TRIP_END_DATE TEXT, $COLUMN_TRIP_LOCATION TEXT, " +
                    "$COLUMN_TRIP_MAX_PARTICIPANTS INTEGER, $COLUMN_TRIP_DESCRIPTION TEXT, " +
                    "$COLUMN_TRIP_STATE)"
        db.execSQL(createTripTableStatement)

        val createParticipantTableStatement =
            "CREATE TABLE $PARTICIPANT_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_PARTICIPANT_NAME, TEXT, " +
                    "$COLUMN_PARTICIPANT_TRIP TEXT REFERENCES $TRIP_TABLE ON DELETE CASCADE ON UPDATE CASCADE)"
        db.execSQL(createParticipantTableStatement)

        val createExpenseTableStatement =
            "CREATE TABLE $EXPENSE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EXPENSE_NAME TEXT, " +
                    "$COLUMN_EXPENSE_AMOUNT REAL, $COLUMN_EXPENSE_TRIP TEXT REFERENCES $TRIP_TABLE ON DELETE CASCADE ON UPDATE CASCADE)"
        db.execSQL(createExpenseTableStatement)
    }

    /**
     * this is called if the database version number changes. It prevents previous apps from breaking when you change the database design
     * @param db: SQLiteDatabase to execute SQL queries
     * @param p1 old version
     * @param p2 new version
     */
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Might not be implemented")
    }

}