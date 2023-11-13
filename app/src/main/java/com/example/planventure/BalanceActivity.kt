package com.example.planventure

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planventure.databinding.ActivityBalanceBinding
import com.example.planventure.databinding.ActivityCreateTripBinding
import com.example.planventure.databinding.FragmentMyTripsBinding
import com.example.planventure.repository.ParticipantExpenseRepository
import com.example.planventure.repository.ParticipantParticipantRepository
import com.example.planventure.repository.ParticipantRepository
import com.example.planventure.repository.TripRepository
import com.example.planventure.utility.BalanceAdapter

class BalanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBalanceBinding
    private lateinit var balanceAdapter: BalanceAdapter

    private lateinit var tripRepository: TripRepository
    private lateinit var participantParticipantRepository: ParticipantParticipantRepository
    private lateinit var participantRepository: ParticipantRepository

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        binding = ActivityBalanceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tripRepository = TripRepository(applicationContext)
        participantParticipantRepository = ParticipantParticipantRepository(applicationContext)
        participantRepository = ParticipantRepository(applicationContext)

        val tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)

        balanceAdapter = BalanceAdapter(getDepths(tripId), applicationContext)

        binding.depthRV.adapter = balanceAdapter
        binding.depthRV.layoutManager = LinearLayoutManager(this)

        binding.balanceBackButton.setOnClickListener {
            this.finish()
        }

        binding.textView12.text = "Balances"





    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getDepths(tripId: Long): ArrayList<Triple<String, String, Float>> {
        val debts = ArrayList<Triple<String, String, Float>>()

        val trip = tripRepository.getById(tripId)
        trip?.getParticipants()?.forEach { p ->

            participantParticipantRepository.findAll().forEach{
                if(p.getId() == it.first.toLong()){
                    debts.add(Triple(
                        participantRepository.getById(it.first.toLong())!!.getName(),
                        participantRepository.getById(it.second.toLong())!!.getName(),
                        it.third
                    ))
                }
            }
        }

        return debts
    }
}