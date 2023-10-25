package com.example.planventure.utility

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R
import com.example.planventure.entity.Participant
import com.example.planventure.service.ParicipantService
import com.example.planventure.service.TripService

@RequiresApi(Build.VERSION_CODES.P)
/**
 * Adapter of a RecyclerViewer for a list of participants
 */
class ParticipantsAdapter(
    private var participants: ArrayList<Participant>,
    private val applicationContext: Context
) : RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder>() {

    private val participantService = ParicipantService(applicationContext, this)
    private val tripService = TripService(applicationContext)

    class ParticipantsViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        return ParticipantsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_trip_participants,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
        val currentParticipant = participants[position] // selected participant in the rv

        holder.itemView.apply {

            var deleteButton = findViewById<Button>(R.id.deleteParticipant_Button_fragment)
            findViewById<TextView>(R.id.participant_textView_fragment).text =
                currentParticipant.getName()

            deleteButton.setOnClickListener {
                val tripId = participantService.getTripIdByParticipantId(currentParticipant.getId())
                participantService.deleteParticipantById(currentParticipant.getId(), tripId)
            }
        }
    }

    /**
     * sets the participants in the recyclerView to updated list from the database from the trip
     */
    fun updateParticipants(tripId: Long) {
        val trip = tripService.getTripById(tripId)
        if (trip != null)
            this.participants = trip.getParticipants()
        notifyDataSetChanged()
    }

}