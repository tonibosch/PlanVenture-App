package com.example.planventure.interfaces_abstracts

import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper

@RequiresApi(Build.VERSION_CODES.P)
abstract class SQLiteRepository<T>(context: Context, private val table: String): IRepository<T>, DataBaseHelper(context){

    /**
     * used to get all data from an existing table
     * @return List of objects mapped from the database via ER mapping
     */
    override fun findAll(): ArrayList<T>{
        val q = findAllQuery()
        return mapQueryToList(q)
    }

    /**
     * used to get a specific object from the database by id
     * @param id primary key id from the table
     * @return Optional object from database with designated id
     */
    override fun getById(id: Long): T?{
        val q = getByIdQuery(id)
        var e: T? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(q, null)
        if(cursor.moveToFirst()){
            e = buildObjectFromCursor(cursor)
        } // else failure
        cursor.close()
        return e
    }

    /**
     * used to update a single entity in the database
     * @param id primary key to identify the designated entity in the table
     * @param e new entity, that contains the updated data
     * @return boolean that shows wether operation was successful or not
     */
    abstract override fun updateById(id: Long, e: T): Boolean

    /**
     * used to delete every item from a table without dropping the table
     * @return boolean that shows wether operation was successful or not
     */
    override fun deleteAll(): Boolean{
        val db = this.writableDatabase
        val q = deleteAllQuery()
        val cursor = db.rawQuery(q, null)
        return closeAndReturn(cursor)
    }

    /**
     * used to delete a specific object from a table by id
     * @return boolean that shows wether operation was successful or not
     * @param id primary key id from the table
     */
    override fun deleteById(id: Int): Boolean{
        val db = this.writableDatabase
        val q = deleteByIdQuery(id)
        val cursor = db.rawQuery(q, null)
        return closeAndReturn(cursor)
    }


    abstract fun mapQueryToList(query: String): ArrayList<T>

    abstract fun buildObjectFromCursor(c: Cursor): T


    private fun findAllQuery(): String {
        return "SELECT * FROM $table"
    }
    private fun getByIdQuery(id: Long): String {
        return "SELECT * FROM $table WHERE ID = $id"
    }
    private fun deleteAllQuery(): String{
        return "DELETE FROM $table"
    }
    private fun deleteByIdQuery(id: Int): String{
        return "DELETE FROM $table WHERE ID = $id"
    }

    fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }


}