package com.example.planventure.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * DataBaseHelper.kt
 * Abstract class to implement local SQLite database
 * Extends DatabaseHelper to get access to local local SQLite database and accompanying functions.
 * @property onCreate(db: SQLiteDatabase)
 * @property onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int)
 *
 * @constructor (context: Context)
 *
 * @see SQLiteOpenHelper
 */
@RequiresApi(Build.VERSION_CODES.P)
open class DataBaseHelper(
    context: Context
) : SQLiteOpenHelper(context, "planventure.db", null, 14) {

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

        const val EXPENSE_TABLE: String = "EXPENSE_TABLE"
        const val COLUMN_EXPENSE_NAME: String = "EXPENSE_NAME"
        const val COLUMN_EXPENSE_AMOUNT: String = "EXPENSE_AMOUNT"
        const val COLUMN_EXPENSE_PAYER_PARTICIPATES: String = "EXPENSE_PAYER_PARTICIPATES"

        const val COLUMN_TRIP_FOREIGN_KEY: String = "TRIP_FOREIGN_KEY"

        const val PARTICIPANT_EXPENSE_TABLE: String = "PARTICIPANT_EXPENSE_TABLE"
        const val COLUMN_PARTICIPANT_ID: String = "PARTICIPANT_ID"
        const val COLUMN_EXPENSE_ID: String = "EXPENSE_ID"
        const val COLUMN_SPENT_AMOUNT: String = "AMOUNT"

        const val PARTICIPANT_PARTICIPANT_TABLE: String = "PARTICIPANT_PARTICIPANT_TABLE"
        const val COLUMN_PARTICIPANT_1_ID: String = "PARTICIPANT_1_ID"
        const val COLUMN_PARTICIPANT_2_ID: String = "PARTICIPANT_2_ID"
        const val COLUMN_PAID_AMOUNT: String = "PAID_AMOUNT"

    }

    /**
     * called the first time a database is accessed. There should be code in here to create a new database
     * @param db: SQLiteDatabase to execute SQL queries
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTripTableStatement =
            "CREATE TABLE $TRIP_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TRIP_NAME TEXT, " +
                    "$COLUMN_TRIP_START_DATE TEXT, $COLUMN_TRIP_END_DATE TEXT, $COLUMN_TRIP_LOCATION TEXT, " +
                    "$COLUMN_TRIP_MAX_PARTICIPANTS INTEGER, $COLUMN_TRIP_DESCRIPTION TEXT, " +
                    "$COLUMN_TRIP_STATE TEXT)"
        db.execSQL(createTripTableStatement)

        val createParticipantTableStatement =
            "CREATE TABLE $PARTICIPANT_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_PARTICIPANT_NAME TEXT, " +
                    "$COLUMN_TRIP_FOREIGN_KEY INTEGER REFERENCES $TRIP_TABLE ON DELETE CASCADE ON UPDATE CASCADE)"
        db.execSQL(createParticipantTableStatement)

        val createExpenseTableStatement =
            "CREATE TABLE $EXPENSE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EXPENSE_NAME TEXT, " +
                    "$COLUMN_EXPENSE_AMOUNT REAL, $COLUMN_TRIP_FOREIGN_KEY INTEGER REFERENCES $TRIP_TABLE ON DELETE CASCADE ON UPDATE CASCADE," +
                    "$COLUMN_EXPENSE_PAYER_PARTICIPATES INTEGER)"
        db.execSQL(createExpenseTableStatement)

        val createExpenseParticipantTable =
            "CREATE TABLE $PARTICIPANT_EXPENSE_TABLE ($COLUMN_PARTICIPANT_ID INTEGER REFERENCES $PARTICIPANT_TABLE ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "$COLUMN_EXPENSE_ID INTEGER REFERENCES $EXPENSE_TABLE ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "$COLUMN_SPENT_AMOUNT NUMBER)"
        db.execSQL(createExpenseParticipantTable)

        val createParticipantParticipantTable =
            "CREATE TABLE $PARTICIPANT_PARTICIPANT_TABLE ($COLUMN_PARTICIPANT_1_ID INTEGER REFERENCES $PARTICIPANT_TABLE ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "$COLUMN_PARTICIPANT_2_ID INTEGER REFERENCES $PARTICIPANT_TABLE ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "$COLUMN_PAID_AMOUNT NUMBERS)"
        db.execSQL(createParticipantParticipantTable)

    }

    /**
     * this is called if the database version number changes. It prevents previous apps from breaking when you change the database design
     * @param db: SQLiteDatabase to execute SQL queries
     * @param p1 old version
     * @param p2 new version
     */
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        val dropTripTableStatement =
            "DROP TABLE IF EXISTS $TRIP_TABLE"
        db.execSQL(dropTripTableStatement)

        val dropParticipantTableStatement =
            "DROP TABLE IF EXISTS $PARTICIPANT_TABLE"
        db.execSQL(dropParticipantTableStatement)

        val dropExpenseTableStatement =
            "DROP TABLE IF EXISTS $EXPENSE_TABLE"
        db.execSQL(dropExpenseTableStatement)

        val dropExpPartTable =
            "DROP TABLE IF EXISTS $PARTICIPANT_EXPENSE_TABLE"
        db.execSQL(dropExpPartTable)

        val dropPartPartTable =
            "DROP TABLE IF EXISTS $PARTICIPANT_PARTICIPANT_TABLE"
        db.execSQL(dropPartPartTable)

        onCreate(db)
    }

}