package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.interfaces_abstracts.IRepository
import com.example.planventure.interfaces_abstracts.SQLiteRepository


/**
 * ParticipantExpenseRepository.kt
 * Repository to store and receive date from the PARTICIPANT_EXPENSE_TABLE in the database
 * Extends SQLiteRepository to access reading and writing functions
 * @property getById(id: Long): Triple<Int, Int, Float>?
 * @property updateById(id: Long, e: Triple<Int, Int, Float>): Boolean
 * @property deleteById(id: Int): Boolean
 * @property getByExpenseId(pid: Long, eid: Long): ArrayList<Triple<Int, Int, Float>>
 * @property deleteByExpenseParticipantId(eid: Int, pid: Int): Boolean
 * @property buildContentValues(e: Triple<Int, Int, Float>): ContentValues
 * @property buildObjectFromCursor(c: Cursor): Triple<Int, Int, Float>
 *
 * @constructor (context: Context)
 *
 * @see IRepository
 * @see SQLiteRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
class ParticipantExpenseRepository(context: Context):
    SQLiteRepository<Triple<Int, Int, Float>, Int>(context, PARTICIPANT_EXPENSE_TABLE) {


    /**
     * Is not implemented on purpose.
     * Do not implement.
     * Do not use.
     */
    override fun getById(id: Long): Triple<Int, Int, Float>? {
        //
        return null
    }

    /**
     * Is not implemented on purpose.
     * Do not implement.
     * Do not use.
     */
    override fun updateById(id: Long, e: Triple<Int, Int, Float>): Boolean {
        //
        return false
    }

    /**
     * Is not implemented on purpose.
     * Do not implement.
     * Do not use.
     */
    override fun deleteById(id: Int): Boolean {
        //
        return false
    }

    /**
     * gets the triple with participant id, expense id and paid amount
     * @param eid : foreign key of the expense
     * @return ArrayList of Triples with participant id, expense id and paid amount
     */
    fun getByExpenseId(eid: Long): ArrayList<Triple<Int, Int, Float>> {
        /*
         * Query to get all rows with given participant ids
         */
        val query = "SELECT * FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_EXPENSE_ID = $eid"
        return read(query)
    }

    /**
     * deletes all rows filtered by the given participant id and expense id
     * @param eid : foreign key of the first participant
     * @param pid : foreign key of the second participant
     * @return either success or fail
     */
    fun deleteByExpenseParticipantId(eid: Int, pid: Int): Boolean {
        /*
         * Query to delete all rows with given participant id and expense id
         */
        val string = "DELETE FROM $PARTICIPANT_EXPENSE_TABLE WHERE $COLUMN_PARTICIPANT_ID = $pid AND $COLUMN_EXPENSE_ID = $eid"
        return delete(string)
    }

    override fun buildContentValues(e: Triple<Int, Int, Float>): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_PARTICIPANT_ID, e.first)
        cv.put(COLUMN_EXPENSE_ID, e.second)
        cv.put(COLUMN_SPENT_AMOUNT, e.third)
        return cv
    }

    override fun buildObjectFromCursor(c: Cursor): Triple<Int, Int, Float> {
        val pid = c.getInt(0)
        val eid = c.getInt(1)
        val amount = c.getFloat(2)
        return Triple(
            /* participant id */    pid,
            /* expense id */        eid,
            /* paid amount */       amount
        )
    }

}