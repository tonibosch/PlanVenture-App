package com.example.planventure.interfaces

/**
 * Interface to collect the main functions of a repository
 */
interface IRepository<T> {

    /**
     * used to get all data from an existing table
     * @return List of objects mapped from the database via ER mapping
     */
    fun findAll(): List<T>

    /**
     * used to get a specific object from the database by id
     * @param id primary key id from the table
     * @return Optional object from database with designated id
     */
    fun getById(id: Long): T?

    /**
     * used to delete every item from a table without dropping the table
     * @return boolean that shows wether operation was successful or not
     */
    fun deleteAll(): Boolean

    /**
     * used to delete a specific object from a table by id
     * @return boolean that shows wether operation was successful or not
     * @param id primary key id from the table
     */
    fun deleteById(id: Int): Boolean

}