package com.example.planventure.interfaces_abstracts

/**
 * Interface to collect the main functions of a repository
 */
interface IRepository<T> {

    /**
     * used to get all data from an existing table
     * @return List of objects mapped from the database via ER mapping
     */
    fun findAll(): ArrayList<T>

    /**
     * used to get a specific object from the database by id
     * @param id primary key id from the table
     * @return Optional object from database with designated id
     */
    fun getById(id: Long): T?

    /**
     * used to update a single entity in the database
     * @param id primary key to identify the designated entity in the table
     * @param e new entity, that contains the updated data
     * @return boolean that shows wether operation was successful or not
     */
    fun updateById(id: Long, e: T): Boolean

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