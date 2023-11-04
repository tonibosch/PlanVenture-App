package com.example.planventure.entity

/**
 * Participant.kt
 * Value object that stores the attributes of a participant
 * @property id
 * @property name
 *
 * @constructor (id: Long, name: String)
 *
 * @see Trip
 */
class Participant(
    /*
     * id to identify participant in the database
     */
    private var id: Long,
    /*
     * name to identify participant in the UI
     */
    private var name: String
) {

    fun getId(): Long{return id}
    fun getName(): String{return name}
}