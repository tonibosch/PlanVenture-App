package com.example.planventure.entity

import com.example.planventure.enumerations.TRIP_STATE
import java.util.Date

/**
 * Trip.kt
 * Value object that stores the attributes of a trip
 * @property id
 * @property name
 * @property startDate
 * @property endDate
 * @property location
 * @property maxNumberOfParticipants
 * @property description
 * @property participants
 * @property expenses
 * @property participants
 * @property tripState
 *
 * @constructor
 *
 * @see Participant
 * @see Expense
 * @see TRIP_STATE
 */
class Trip(
    /*
     * id to identify trip in database
     */
    private var id: Long,
    /*
     * name to identify trip in UI
     */
    private var name: String,
    /*
     * date when the trip begins with format yyyy-MM-dd
     */
    private var startDate: Date,
    /*
     * date when the trip ends with format yyyy-MM-dd
     */
    private var endDate: Date,
    /*
     * location as String where the trip is supposed to take place
     */
    private var location: String,
    /*
     * maximum number of participants, that is allowed to participate at the trip
     */
    private var maxNumberOfParticipants: Int,
    /*
     * small description of the trip and how the trip will look like
     */
    private var description: String,
    /*
     * list of all participating persons
     */
    private var participants: ArrayList<Participant>,
    /*
     * list of all expenses that occurred during the trip
     */
    private var expenses: ArrayList<Expense>,
    /*
     * State of the trip.
     * Can be planning, started or finished
     */
    private var tripState: TRIP_STATE
) {

    override fun toString(): String {

        val sb = StringBuilder("")
        sb.append("$id, $name, $startDate, $location", "$tripState")

        return sb.toString()
    }


    fun getId(): Long{return id}
    fun getName(): String{return name}
    fun getStartDate(): Date{return startDate}
    fun getEndDate(): Date{return endDate}
    fun getLocation(): String{return location}
    fun getMaxNumberOfParticipants(): Int{return maxNumberOfParticipants}
    fun getDescription(): String{return description}
    fun getState(): TRIP_STATE{return tripState}
    fun getParticipants(): ArrayList<Participant>{return participants}
    fun getExpenses(): ArrayList<Expense>{return expenses}
    fun setParticipants(p: ArrayList<Participant>){participants = p}
    fun setExpenses(e: ArrayList<Expense>){expenses = e}

}