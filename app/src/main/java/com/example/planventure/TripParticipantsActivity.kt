package com.example.planventure

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.entity.Participant
import com.example.planventure.service.ParicipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ParticipantsAdapter
import java.util.ArrayList

class TripParticipantsActivity : AppCompatActivity() {

    private lateinit var participantService: ParicipantService
    private lateinit var tripService: TripService
    private lateinit var participantsAdapter: ParticipantsAdapter
    private lateinit var addButton: Button
    private lateinit var addTextEdit: EditText
    private lateinit var participantRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_participants)

        val tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)

        participantsAdapter = ParticipantsAdapter(ArrayList<Participant>(), applicationContext)
        participantService = ParicipantService(applicationContext, participantsAdapter)

        participantsAdapter.updateParticipants(tripId)
        tripService = TripService(applicationContext)

        addButton = findViewById(R.id.addParticipant_Button_participants)
        addTextEdit = findViewById(R.id.addParticipant_editText_participants)

        participantRecyclerView = findViewById(R.id.participant_recyclerView_participants)
        participantRecyclerView.adapter = participantsAdapter
        participantRecyclerView.layoutManager = LinearLayoutManager(this)

        val trip = tripService.getTripById(tripId)

        addButton.setOnClickListener {
            try {
                val participant: Participant =
                    Participant(participantService.getSize() + 1, addTextEdit.text.toString())
                if (trip != null) {
                    participantService.addParticipantToDb(participant, trip)
                }
            } catch (e:EmptyDataException){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e:MaxParticipantsOverflow){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}