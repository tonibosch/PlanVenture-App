package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.databinding.ActivityCreateTripBinding
import com.example.planventure.databinding.ActivityTripInformationBinding
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.ParticipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class TripInformationActivity : AppCompatActivity() {

    companion object {
        const val TRIP_ID_TRIP_PARTICIPANTS = "com.example.planventure.TripInformation.TripId"
    }

    private lateinit var binding: ActivityTripInformationBinding
    private lateinit var tripService: TripService
    private lateinit var participantService: ParticipantService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tripService = TripService(applicationContext)
        participantService = ParticipantService(applicationContext)

        val tripId = intent.getLongExtra(MyTripsFragment.TRIP_ID_TRIP_INFORMATION,0)
        val trip = tripService.getTripById(tripId)

        binding.tripNameEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getName())
        binding.endDateTextViewTripInformation.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getStartDate().toString()))
        binding.endDateTextViewTripInformation.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getEndDate().toString()))
        binding.locationEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getLocation())
        binding.maxPartNumberEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getMaxNumberOfParticipants().toString())
        binding.tripDescriptionEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getDescription())

        binding.backButtonTripInfo.setOnClickListener {
            this.finish()
        }

        var currentStatus = trip?.getState()
        Log.d("CURRENT STATUS", "${currentStatus.toString()}")

        when (currentStatus) {
            TRIP_STATE.PLANNING -> binding.buttonChangeState.text = "START"
            TRIP_STATE.STARTED -> {
                binding.buttonChangeState.text = "FINISH"
                disableEditText()
            }
            else -> {
                binding.buttonChangeState.text = "FINISHED"
                disableEditText()
            }
        }

        binding.buttonChangeState.setOnClickListener {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            if(currentStatus == TRIP_STATE.PLANNING) {
                binding.buttonChangeState.text = "FINISH"
                currentStatus = TRIP_STATE.STARTED
                disableEditText()
                tripService.updateTrip(tripId, Trip(1, binding.tripNameEditTextTripInformation.text.toString(), formatter.parse(binding.startDateTextViewTripInformation.text.toString()),
                    formatter.parse(binding.endDateTextViewTripInformation.text.toString()), binding.locationEditTextTripInformation.text.toString(), binding.maxPartNumberEditTextTripInformation.text.toString().toInt(),
                    binding.tripDescriptionEditTextTripInformation.text.toString(), ArrayList(), ArrayList(),TRIP_STATE.STARTED))
                Toast.makeText(this, "You have started the trip", Toast.LENGTH_SHORT).show()
            }
            else if(currentStatus == TRIP_STATE.STARTED){
                binding.buttonChangeState.text = "FINISHED"
                currentStatus = TRIP_STATE.FINISHED
                tripService.updateTrip(tripId, Trip(1, binding.tripNameEditTextTripInformation.text.toString(), formatter.parse(binding.startDateTextViewTripInformation.text.toString()),
                    formatter.parse(binding.endDateTextViewTripInformation.text.toString()), binding.locationEditTextTripInformation.text.toString(), binding.maxPartNumberEditTextTripInformation.text.toString().toInt(),
                    binding.tripDescriptionEditTextTripInformation.text.toString(),ArrayList(), ArrayList(), TRIP_STATE.FINISHED))
                Toast.makeText(this, "You have finished the trip", Toast.LENGTH_SHORT).show()
            }
        }

        binding.dateRangePickerButtonTripInformation.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager, binding.startDateTextViewTripInformation, binding.endDateTextViewTripInformation)
            datePicker.showDateRangePicker()
        }

        binding.ExpensesButton.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            startActivity(intent)
        }

        binding.updateTripButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            try {
                //println("DESCRIPTION" + description.text.toString())
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                tripService.updateTrip(
                    tripId, Trip(
                        1,
                        binding.tripNameEditTextTripInformation.text.toString(),
                        formatter.parse(binding.startDateTextViewTripInformation.text.toString()),
                        formatter.parse(binding.endDateTextViewTripInformation.text.toString()),
                        binding.locationEditTextTripInformation.text.toString(),
                        binding.maxPartNumberEditTextTripInformation.text.toString().toInt(),
                        binding.tripDescriptionEditTextTripInformation.text.toString(),
                        ArrayList(),
                        ArrayList(),
                        currentStatus!!
                    )
                )
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.gotoParticipantsButtonTripInformation.setOnClickListener {
            val intent = Intent(applicationContext, TripParticipantsActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            startActivity(intent)
        }

        binding.cancelButtonTripInformation.setOnClickListener{
            this.finish()
        }

    }
    private fun disableEditText() {
        binding.tripNameEditTextTripInformation.isEnabled = false
        binding.startDateTextViewTripInformation.isEnabled = false
        binding.endDateTextViewTripInformation.isEnabled = false
        binding.locationEditTextTripInformation.isEnabled = false
        binding.maxPartNumberEditTextTripInformation.isEnabled = false
        binding.tripDescriptionEditTextTripInformation.isEnabled = false
    }
}

fun convertDate (inputDate: String): String {
    val inputFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val date = inputFormat.parse(inputDate)
    val formattedDate = outputFormat.format(date)
    return formattedDate
}

