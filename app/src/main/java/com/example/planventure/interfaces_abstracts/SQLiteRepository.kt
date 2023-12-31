package com.example.planventure.interfaces_abstracts

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * SQLiteRepository.kt
 * Abstract class to collect the main functions of a repository used for the PlanVenture app
 * Extends CRUDSQLiteRepository to get access to CRUD operations on local SQLite database.
 * Inherits from IRepository to implement needed functionality for PlanVenture app
 * @property findAllQuery Query to get all Data from a table
 * @property getByIdQuery Query to get specific date from a table filtered by id
 * @property deleteAllQuery Query to delete all data from a table
 * @property deleteByIdQuery Query to delete specific data from a table filtered by id
 *
 * @property addToDB(p: Pair<T, K>): Boolean
 * @property findAll(): ArrayList<T>
 * @property getById(id: Long): T?
 * @property updateById(id: Long, e: T): Boolean
 * @property deleteAll(): Boolean
 * @property deleteById(id: Int): Boolean
 *
 * @constructor (context: Context, table: String)
 *
 * @see IRepository
 * @see CRUDRepository
 */
@RequiresApi(Build.VERSION_CODES.P)
abstract class SQLiteRepository<T, K>(context: Context, table: String):
    CRUDSQLiteRepository<T>(context, table), IRepository<T, K>{

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

    
    override fun addToDB(p: Pair<T, K>): Boolean{
        /*
         * put all data into content values
         */
        val cv = buildContentValues(p.first)

        val foreignKey = p.second.toString().toInt()

        /*
         * add the foreign key for the related trip
         */
        if(foreignKey >= 0)
            cv.put(COLUMN_TRIP_FOREIGN_KEY, foreignKey)

        /*
         * insert the values into the DB and return the result
         * result is either successful of fail
         */
        return create(cv)
    }

    override fun findAll(): ArrayList<T>{
        /*
         * use the findAllQuery as input for get
         * return the resulting ArrayList
         */
        return read(findAllQuery)
    }

    override fun getById(id: Long): T?{
        /*
         * use the getByIdQuery and append the id of the desired object
         * return the first and only element from the resulting ArrayList
         */
        return read("$getByIdQuery $id")[0]
    }

    override fun updateById(id: Long, e: T): Boolean{
        /*
         * store all attributes from the object in content values
         */
        val cv = buildContentValues(e)

        /*
         * update the designated table entry filtered by the id
         */
        return update(id, cv)
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

}