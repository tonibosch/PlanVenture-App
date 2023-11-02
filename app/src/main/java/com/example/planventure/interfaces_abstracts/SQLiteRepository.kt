package com.example.planventure.interfaces_abstracts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.database.DataBaseHelper

@RequiresApi(Build.VERSION_CODES.P)
abstract class SQLiteRepository<T>(context: Context, private val table: String): IRepository<T>, DataBaseHelper(context){

    private val findAllQuery = "SELECT * FROM $table"

    private val getByIdQuery = "SELECT * FROM $table WHERE ID ="

    private val deleteAllQuery = "DELETE FROM $table"

    private val deleteByIdQuery = "DELETE FROM $table WHERE ID ="

    val wdb: SQLiteDatabase = writableDatabase

    val rdb: SQLiteDatabase = readableDatabase

    override fun findAll(): ArrayList<T>{
        return mapQueryToList(findAllQuery)
    }

    override fun getById(id: Long): T?{
        return mapQueryToList("$getByIdQuery $id")[0]
    }

    override fun updateById(id: Long, e: T): Boolean{
        val cv = buildContentValues(e)
        return when(wdb.update(TRIP_TABLE, cv, "ID=?", arrayOf(id.toString()))) {-1 -> false else -> true}
    }

    override fun deleteAll(): Boolean{
        return delete(deleteAllQuery)
    }

    override fun deleteById(id: Int): Boolean{
        return delete("$deleteByIdQuery $id")
    }

    abstract fun mapQueryToList(query: String): ArrayList<T>

    abstract fun buildObjectFromCursor(c: Cursor): T

    abstract fun buildContentValues(e: T): ContentValues

    fun closeAndReturn(c: Cursor):Boolean{
        return when(c.moveToFirst()){true -> {c.close(); true}else -> false}
    }
    private fun delete(q: String): Boolean{
        val cursor = wdb.rawQuery(q, null)
        return closeAndReturn(cursor)
    }


}