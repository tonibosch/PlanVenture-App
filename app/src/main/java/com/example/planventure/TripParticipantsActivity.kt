package com.example.planventure

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.databinding.ActivityCreateExpenseBinding
import com.example.planventure.databinding.ActivityTripParticipantsBinding
import com.example.planventure.entity.Participant
import com.example.planventure.service.ParticipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ParticipantsAdapter
import java.util.ArrayList

class TripParticipantsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTripParticipantsBinding
    //Services
    private lateinit var participantService: ParticipantService
    private lateinit var tripService: TripService
    //Adapters
    private lateinit var participantsAdapter: ParticipantsAdapter

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripParticipantsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)

        participantsAdapter = ParticipantsAdapter(ArrayList<Participant>(), applicationContext)
        participantService = ParticipantService(applicationContext, participantsAdapter)
        participantsAdapter.updateParticipants(tripId)
        binding.participantRecyclerViewParticipants.adapter = participantsAdapter
        binding.participantRecyclerViewParticipants.layoutManager = LinearLayoutManager(this)

        tripService = TripService(applicationContext)
        val trip = tripService.getTripById(tripId)

        //Define buttons behavior
        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButtonTripParticipants.setOnClickListener {
            this.finish()
        }

        //Configures the button to add a participant to the trip. If there is no error, the participant is created and appears on the screen. If there is an error a Toast message will be displayed.
        binding.addParticipantButtonParticipants.setOnClickListener {
            try {
                val participant: Participant = Participant(1, binding.addParticipantEditTextParticipants.text.toString())
                if (trip != null) {
                    participantService.addParticipantToDb(participant, trip)
                    binding.addParticipantEditTextParticipants.text.clear()
                }
            } catch (e:EmptyDataException){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e:MaxParticipantsOverflow){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}