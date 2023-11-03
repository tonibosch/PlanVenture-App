package com.example.planventure.interfaces_abstracts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper

@RequiresApi(Build.VERSION_CODES.P)
abstract class SQLiteRepository<T, K>(context: Context, private val table: String): IRepository<T, K>, DataBaseHelper(context){

    /**
     * Query to get all Data from a table
     */
    private val findAllQuery = "SELECT * FROM $table"

    /**
     * Query to get specific date from a table filtered by id
     */
    private val getByIdQuery = "SELECT * FROM $table WHERE ID ="

    /**
     * Query to delete all data from a table
     */
    private val deleteAllQuery = "DELETE FROM $table"

    /**
     * Query to delete specific data from a table filtered by id
     */
    private val deleteByIdQuery = "DELETE FROM $table WHERE ID ="

    /**
     * writable DB reference to perform writing operations on the DB like update, delete, insert, ...
     */
    val wdb: SQLiteDatabase = writableDatabase

    /**
     * readable DB reference to perform reading operations on the DB
     */
    val rdb: SQLiteDatabase = readableDatabase

    /*
     * needs to be implemented by the inheriting class
     */
    abstract override fun addToDB(e: Pair<T, K>): Boolean

    override fun findAll(): ArrayList<T>{
        /*
         * use the findAllQuery as input for mapQueryToList
         * return the resulting ArrayList
         */
        return mapQueryToList(findAllQuery)
    }

    override fun getById(id: Long): T?{
        /*
         * use the getByIdQuery and append the id of the desired object
         * return the first and only element from the resulting ArrayList
         */
        return mapQueryToList("$getByIdQuery $id")[0]
    }

    override fun updateById(id: Long, e: T): Boolean{
        /*
         * store all attributes from the object in content values
         */
        val cv = buildContentValues(e)

        /*
         * update the designated table entry filtered by the id
         * update function takes a input:
         * - table name
         * - content values
         * - where clause
         * - array of where arguments that are placed behind the where clause
         * when the operation was successful return true else return false
         */
        return when(wdb.update(TRIP_TABLE, cv, "ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}
    }

    override fun deleteAll(): Boolean{
        /*
         * use the deleteAllQuery to delete all table entries
         */
        return delete(deleteAllQuery)
    }

    override fun deleteById(id: Int): Boolean{
        /*
         * use the deleteById Query and append the id of the desired DB entry to delete it
         */
        return delete("$deleteByIdQuery $id")
    }

    /**
     * Used to execute a reading Query operation and store the DB output in a List.
     * Needs to be adjusted manually by the inheriting class.
     * @param query :String with the SQL-Query
     * @return ArrayList<T> List with all found objects
     */
    abstract fun mapQueryToList(query: String): ArrayList<T>

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
    fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }

    /**
     * deletes all data represented by the input Query
     * @param q :Query string that is supposed to be executed
     * @return either success or fail
     */
    private fun delete(q: String): Boolean{
        val cursor = wdb.rawQuery(q, null)
        return closeAndReturn(cursor)
    }


}