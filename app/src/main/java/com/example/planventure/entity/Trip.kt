package com.example.planventure.entity

import com.example.planventure.enumerations.TRIP_STATE
import java.util.Date

class Trip(
    private var id: Long,
    private var name: String,
    private var startDate: Date,
    private var endDate: Date,
    private var location: String,
    private var maxNubmerOfParticipants: Int,
    private var description: String,
    private var participants: ArrayList<Participant>,
    private var expences: ArrayList<Expense>,
    private var tripState: TRIP_STATE
) {

    override fun toString(): String {

        val sb = StringBuilder("")
        sb.append("$name, $startDate, $location")

        return sb.toString()
    }


    fun getId(): Long{return id}
    fun getName(): String{return name}
    fun getStartDate(): Date{return startDate}
    fun getEndDate(): Date{return endDate}
    fun getLocation(): String{return location}
    fun getMaxNumberOfParticipants(): Int{return maxNubmerOfParticipants}
    fun getDescription(): String{return description}
    fun getState(): TRIP_STATE{return tripState}
    fun getParticipants(): ArrayList<Participant>{return participants}
    fun getExpenses(): ArrayList<Expense>{return expences}

    fun setParticipants(p: ArrayList<Participant>){participants = p}
    fun setExpenses(e: ArrayList<Expense>){expences = e}

}