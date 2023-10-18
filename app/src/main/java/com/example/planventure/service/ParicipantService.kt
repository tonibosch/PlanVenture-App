package com.example.planventure.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ParticipantRepository
import com.example.planventure.utility.ParticipantsAdapter

@RequiresApi(Build.VERSION_CODES.P)
class ParicipantService(
    private val applicationContext: Context,
    private val participantsAdapter: ParticipantsAdapter? = null
) {

    private val participantRepository = ParticipantRepository(applicationContext)

    /**
     * add participant to the database after checking if there is still space for more participants and if
     * the name for the participant is not empty
     */
    fun addParticipantToDb(p: Participant, t: Trip) {
        if (p.getName().isEmpty()) {
            throw EmptyDataException("name can not be empty")
        } else if (getParticipantsByTrip(t).size + 1 > t.getMaxNumberOfParticipants()) {
            throw MaxParticipantsOverflow("reached maximum number of Participants")
        } else {
            participantRepository.addParticipantToDb(p, t.getId().toInt())
            participantsAdapter?.updateParticipants(t.getId())
        }
    }

    fun getSize(): Long {
        return participantRepository.getSize()
    }

    fun getParticipantsByTrip(t: Trip): ArrayList<Participant> {
        return participantRepository.getParticipantsByTrip(t)
    }

    fun getParticipantsByTripId(id: Long): ArrayList<Participant> {
        return participantRepository.getParticipantsByTripId(id)
    }

    fun getAllParticipants(): ArrayList<String> {
        val temp = participantRepository.findAll()
        val participants = ArrayList<String>()
        temp.forEach {
            participants.add(it.getName())
        }
        return participants
    }

    fun deleteParticipantById(id: Long, tripId: Long) {
        participantRepository.deleteById(id.toInt())
        participantsAdapter?.updateParticipants(tripId)
    }

    fun getTripIdByParticipantId(id: Long): Long {
        val trip = participantRepository.getTripByParticipantId(id.toInt())
        if (trip != null) {
            return trip.getId()
        } else {
            throw Error("trip can not be empty")
        }
    }

}