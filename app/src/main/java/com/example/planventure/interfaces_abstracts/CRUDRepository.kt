package com.example.planventure.interfaces_abstracts

import android.content.ContentValues

/**
 * CRUDRepository.kt
 * Interface to collect CRUD functions
 * @property create(cv: ContentValues): Boolean
 * @property read(q: String): ArrayList<T>
 * @property update(id: Long, cv: ContentValues): Boolean
 * @property delete(q: String): Boolean
 */
interface CRUDRepository<T> {

    /**
     * stores data from the content values in the database
     * @param cv :ContentValues with objects data
     * @return either success or fail
     */
    fun create(cv: ContentValues): Boolean

    /**
     * gets all data represented by the input Query
     * @param q :Query string that is supposed to be executed
     * @return either success or fail
     */
    fun read(q: Query): ArrayList<T>

    /**
     * updates existing data on the database
     * @param id : primary key of entry which is supposed to be updated
     * @param cv : values that are supposed to be stored
     * @return either success or fail
     */
    fun update(id: Long, cv: ContentValues): Boolean

    /**
     * deletes all data represented by the input Query
     * @param q :Query string that is supposed to be executed
     * @return either success or fail
     */
    fun delete(q: Query): Boolean

}