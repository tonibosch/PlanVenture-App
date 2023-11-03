package com.example.planventure.interfaces_abstracts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper

@RequiresApi(Build.VERSION_CODES.P)
abstract class CRUDSQLiteRepository<T>(context: Context, private val table: String): CRUDRepository<T>, DataBaseHelper(context){

    /**
     * writable DB reference to perform writing operations on the DB like update, delete, insert, ...
     */
    private val wdb: SQLiteDatabase = writableDatabase

    /**
     * readable DB reference to perform reading operations on the DB
     */
    val rdb: SQLiteDatabase = readableDatabase


    override fun create(cv: ContentValues): Boolean{
        // insert the values into the DB and return the result
        // result is either successful of fail
        return when (wdb.insert(table, null, cv)) {
            -1L  -> false
            else -> true
        }
    }

    override fun read(q: String): ArrayList<T>{
        return mapQueryToList(q)
    }

    override fun update(id: Long, cv: ContentValues): Boolean{
        /*
         * update the designated table entry filtered by the id
         * update function takes a input:
         * - table name
         * - content values
         * - where clause
         * - array of where arguments that are placed behind the where clause
         * when the operation was successful return true else return false
         */
        return when(wdb.update(TRIP_TABLE, cv, "ID=?", arrayOf(id.toString()))) {
            -1  -> false
            else -> true
        }
    }

    override fun delete(q: String): Boolean{
        return execute(q)
    }

    /**
     * used to execute a query where no read operation is needed or expected
     * @param q : Query to execute
     * @return either success or fail
     */
    fun execute(q: String): Boolean{
        val cursor = wdb.rawQuery(q, null)
        return closeAndReturn(cursor)
    }

    /**
     * Used to execute a reading Query operation and store the DB output in a List.
     * Needs to be adjusted manually by the inheriting class.
     * @param query :String with the SQL-Query
     * @return ArrayList<T> List with all found objects
     */
    open fun mapQueryToList(query: String): ArrayList<T>{
        val l: ArrayList<T> = ArrayList()

        // execute query and get cursor
        val cursor = rdb.rawQuery(query, null)

        // if the cursor contains data
        if(cursor.moveToFirst()){

            // extract data while the cursor has data
            do {
                l.add(buildObjectFromCursor(cursor))
            }while (cursor.moveToNext())
        }else{
            // failure. Do not add anything to list
        }
        cursor.close()
        return l
    }

    /**
     * Used to build an object from the data stored in the cursor.
     * Needs to me adjusted manually by the inheriting class.
     * @param c :Cursor with data
     * @return specific object
     */
    abstract fun buildObjectFromCursor(c: Cursor): T

    /**
     * Used to store data from an object in content values to store them in the DB.
     * Needs to be adjusted manually by inheriting class.
     * @param e :object Entity to store
     * @return content values with the data to be stored
     */
    abstract fun buildContentValues(e: T): ContentValues

    /**
     * Closes a cursor after a certain Query execution and returns the success of the operation
     * @param c :Cursor that needs to be closed
     * @return either success or fail
     */
    private fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){
            true -> {c.close(); true}
            else -> false
        }
    }

}
