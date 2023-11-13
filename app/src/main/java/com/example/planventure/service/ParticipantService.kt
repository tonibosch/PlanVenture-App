package com.example.planventure.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ParticipantExpenseRepository
import com.example.planventure.repository.ParticipantRepository
import com.example.planventure.repository.TripRepository
import com.example.planventure.utility.ParticipantsAdapter

@RequiresApi(Build.VERSION_CODES.P)
class ParticipantService(
    private val applicationContext: Context,
    private val participantsAdapter: ParticipantsAdapter? = null
) {

    private val participantRepository = ParticipantRepository(applicationContext)
    private val tripRepository = TripRepository(applicationContext)
    private val participantExpenseRepository = ParticipantExpenseRepository(applicationContext)

    /**
     * add participant to the database after checking if there is still space for more participants and if
     * the name for the participant is not empty
     */
    fun addParticipantToDb(p: Participant, t: Trip) {
        if (p.getName().isEmpty()) {
            throw EmptyDataException("name can not be empty")
        } else if (getParticipantsByTrip(t).size + 1 > t.getMaxNumberOfParticipants()) {
            throw MaxParticipantsOverflow("reached maximum number of Participants")
        } /*else if (checkParticipantInTrip(t, p.getName())){
            throw MultipleNamesException("name is already in the trip")
        }*/
        else {
            participantRepository.addToDB(Pair(p, t.getId().toInt()))
            participantsAdapter?.updateParticipants(t.getId())
        }
    }

    private fun checkParticipantInTrip(t: Trip, pName: String): Boolean {
        val participantNames = ArrayList<String>()
        t.getParticipants().forEach {
            participantNames.add(it.getName())
        }
        return !participantNames.contains(pName)
    }

    fun getParticipantsByTrip(t: Trip): ArrayList<Participant> {
        return participantRepository.getParticipantsByTrip(t)
    }

    fun deleteParticipantById(id: Long, tripId: Long) {
        participantRepository.deleteById(id.toInt())
        participantsAdapter?.updateParticipants(tripId)
    }

    fun getTripIdByParticipantId(id: Long): Long {
        val trip = tripRepository.getTripByParticipantId(id.toInt())
        if (trip != null) {
            return trip.getId()
        } else {
            throw Error("trip can not be empty")
        }
    }

    fun getParticipantByName(pName:String,t:Trip):Participant{
        val participants = participantRepository.getParticipantsByTrip(t)
        return participants.filter { it.getName() == pName }[0]
    }

    fun getParticipantsByExpense(expense: Expense): ArrayList<Pair<Participant?, Boolean>>{
        val listOfParticipant = ArrayList<Pair<Participant?, Boolean>>()
        participantExpenseRepository.getByExpenseId(expense.getId()).forEach{
            if(it.third == 0f){
                listOfParticipant.add(Pair(participantRepository.getById(it.first.toLong()),false))
            } else {
                listOfParticipant.add(Pair(participantRepository.getById(it.first.toLong()),true))
            }
        }
        return listOfParticipant
    }



}