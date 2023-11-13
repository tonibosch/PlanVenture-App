package com.example.planventure.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.interfaces_abstracts.IRepository
import com.example.planventure.interfaces_abstracts.SQLiteRepository

/**
 * ParticipantParticipantRepository.kt
 * Repository to store and receive date from the PARTICIPANT_PARTICIPANT_TABLE in the database
 * Extends SQLiteRepository to access reading and writing functions
 * @property getById(id: Long): Triple<Int, Int, Float>?
 * @property updateById(id: Long, e: Triple<Int, Int, Float>): Boolean
 * @property deleteById(id: Int): Boolean
 * @property getByParticipant1Id(p1id: Long): ArrayList<Triple<Int, Int, Float>>
 * @property deleteByParticipant1and2Id(p1id: Long, p2id: Long): Boolean
 * @property buildContentValues(e: Triple<Int, Int, Float>): ContentValues
 * @property buildObjectFromCursor(c: Cursor): Triple<Int, Int, Float>
 *
 * @constructor (context: Context)
 *
 * @see IRepository
 * @see SQLiteRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
class ParticipantParticipantRepository(context: Context):
    SQLiteRepository<Triple<Int, Int, Float>, Int>(context, PARTICIPANT_PARTICIPANT_TABLE){

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
    override fun deleteById(id: Int): Boolean {
        //
        return false
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
     * gets the triple with participant 1 id, participant 2 id, and paid amount
     * @param p1id : foreign key of the first participant
     * @return ArrayList of Triples with participants and paid amount
     */
    fun getByParticipant1Id(p1id: Long): ArrayList<Triple<Int, Int, Float>> {
        /*
         * Query to get all rows with given participant ids
         */
        val query = "SELECT * FROM $PARTICIPANT_PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_ID = $p1id"
        return read(query)
    }

    /**
     * deletes all rows filtered by the given participant ids
     * @param p1id : foreign key of the first participant
     * @param p2id : foreign key of the second participant
     * @return either success or fail
     */
    fun deleteByParticipant1and2Id(p1id: Long, p2id: Long): Boolean {
        /*
         * Query to delete all rows with given participant ids
         */
        val string = "DELETE FROM $PARTICIPANT_PARTICIPANT_TABLE WHERE $COLUMN_PARTICIPANT_ID = $p1id AND $COLUMN_EXPENSE_ID = $p2id"
        return delete(string)
    }

    override fun buildObjectFromCursor(c: Cursor): Triple<Int, Int, Float> {
        val p1id = c.getInt(0)
        val p2id = c.getInt(1)
        val amount = c.getFloat(2)
        return Triple(
            /* participant 1 id */  p1id,
            /* participant 2 id */  p2id,
            /* paid amount */       amount
        )
    }

    override fun buildContentValues(e: Triple<Int, Int, Float>): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_PARTICIPANT_1_ID, e.first)
        cv.put(COLUMN_PARTICIPANT_2_ID, e.second)
        cv.put(COLUMN_PAID_AMOUNT, e.third)
        return cv
    }


}