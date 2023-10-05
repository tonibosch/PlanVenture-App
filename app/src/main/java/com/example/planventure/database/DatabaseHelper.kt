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
        //TODO("Participants, Expences, State")

        const val PARTICIPANT_TABLE: String = "PARTICIPANT_TABLE"
        const val COLUMN_PARTICIPANT_NAME: String = "PARTICIPANT_NAME"

        const val EXPENSE_TABLE: String = "EXPENSE_TABLE"
        const val COLUMN_EXPENSE_NAME: String = "EXPENSE_NAME"
        const val COLUMN_EXPENSE_AMOUNT: String = "EXPENSE_AMOUNT"
    }

    // called the first time a database is accessed. There should be code in here to create a new database
    override fun onCreate(db: SQLiteDatabase) {
        val createTripTableStatement =
            "CREATE TABLE $TRIP_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TRIP_NAME TEXT, " +
                    "$COLUMN_TRIP_START_DATE TEXT, $COLUMN_TRIP_END_DATE TEXT, $COLUMN_TRIP_LOCATION TEXT, " +
                    "$COLUMN_TRIP_MAX_PARTICIPANTS INTEGER, $COLUMN_TRIP_DESCRIPTION TEXT)"
        db.execSQL(createTripTableStatement)

        val createParticipantTableStatement =
            "CREATE TABLE $PARTICIPANT_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_PARTICIPANT_NAME, TEXT)"
        db.execSQL(createParticipantTableStatement)

        val createExpenseTableStatement =
            "CREATE TABLE $EXPENSE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EXPENSE_NAME TEXT, $COLUMN_EXPENSE_AMOUNT REAL)"
        db.execSQL(createExpenseTableStatement)
    }

    // this is called if the database version number changes. It prevents previous apps from breaking when you change the database design
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Might not be implemented")
    }

}