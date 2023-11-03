package com.example.planventure.interfaces_abstracts

/**
 * IRepository.kt
 * Interface to collect the main functions of a repository
 * Ensures access to basic CRUD operations
 */
interface IRepository<T, K> {

    /**
     * used to store an object in the DB by providing a Pair with the object and in case of relations to other tables a foreign key to such
     * @param e Pair consisting of the Object and a foreign key to related table
     * @return Boolean to check weather operation was successful
     */
    fun addToDB(e: Pair<T, K>): Boolean

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
     * @return boolean that shows whether operation was successful or not
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