package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.databinding.ActivityTripInformationBinding
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.ParticipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * An activity for displaying and managing trip information and status.
 *
 * This activity provides functionality for displaying trip details, editing trip information,
 * changing the trip status, and managing participants and expenses related to the trip.
 *
 * @property binding The binding object for the activity's layout.
 * @property tripService The service responsible for managing trips.
 * @property participantService The service responsible for managing participants.
 */
@RequiresApi(Build.VERSION_CODES.P)
class TripInformationActivity : AppCompatActivity() {

    companion object {
        const val TRIP_ID_TRIP_PARTICIPANTS = "com.example.planventure.TripInformation.TripId"
    }

    private lateinit var binding: ActivityTripInformationBinding
    //Services
    private lateinit var tripService: TripService
    private lateinit var participantService: ParticipantService

    /**
     * Initializes the activity's view and displays information about the selected trip.
     *
     * @param savedInstanceState The saved instance state, if any.
     */
    @SuppressLint("MissingInflatedId", "SetTextI18n")
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
        binding.startDateTextViewTripInformation.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getStartDate().toString()))
        binding.endDateTextViewTripInformation.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getEndDate().toString()))
        binding.locationEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getLocation())
        binding.maxPartNumberEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getMaxNumberOfParticipants().toString())
        binding.tripDescriptionEditTextTripInformation.text = Editable.Factory.getInstance().newEditable(trip?.getDescription())

        //Define buttons behavior
        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButtonTripInfo.setOnClickListener {
            this.finish()
        }

        var currentStatus = trip?.getState()

        when (currentStatus) {
            TRIP_STATE.PLANNING -> binding.buttonChangeState.text = "START"
            TRIP_STATE.STARTED -> {
                binding.buttonChangeState.text = "FINISH"
                disableChangeTripInformation()
            }
            else -> {
                binding.buttonChangeState.text = "FINISHED"
                disableChangeTripInformation()
                disableAddExpensesAndParticipants()
            }
        }

        //Configure the button to change the status. If it is in Planning it becomes Started, and if it is in Started it becomes Finished.
        binding.buttonChangeState.setOnClickListener {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            if(currentStatus == TRIP_STATE.PLANNING) {
                binding.buttonChangeState.text = "FINISH"
                currentStatus = TRIP_STATE.STARTED
                disableChangeTripInformation()
                tripService.updateTrip(tripId, Trip(1, binding.tripNameEditTextTripInformation.text.toString(), formatter.parse(binding.startDateTextViewTripInformation.text.toString()),
                    formatter.parse(binding.endDateTextViewTripInformation.text.toString()), binding.locationEditTextTripInformation.text.toString(), binding.maxPartNumberEditTextTripInformation.text.toString().toInt(),
                    binding.tripDescriptionEditTextTripInformation.text.toString(), ArrayList(), ArrayList(),TRIP_STATE.STARTED))
                Toast.makeText(this, "You have started the trip", Toast.LENGTH_SHORT).show()
            }
            else if(currentStatus == TRIP_STATE.STARTED){
                binding.buttonChangeState.text = "FINISHED"
                currentStatus = TRIP_STATE.FINISHED
                disableAddExpensesAndParticipants()
                tripService.updateTrip(tripId, Trip(1, binding.tripNameEditTextTripInformation.text.toString(), formatter.parse(binding.startDateTextViewTripInformation.text.toString()),
                    formatter.parse(binding.endDateTextViewTripInformation.text.toString()), binding.locationEditTextTripInformation.text.toString(), binding.maxPartNumberEditTextTripInformation.text.toString().toInt(),
                    binding.tripDescriptionEditTextTripInformation.text.toString(),ArrayList(), ArrayList(), TRIP_STATE.FINISHED))
                Toast.makeText(this, "You have finished the trip", Toast.LENGTH_SHORT).show()
            }
        }

        //Configure the button to select the dates of the trip. Clicking the button opens a calendar for the user to select the dates.
        binding.dateRangePickerButtonTripInformation.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager, binding.startDateTextViewTripInformation, binding.endDateTextViewTripInformation)
            datePicker.showDateRangePicker()
        }

        //Configure the "Expenses" button to navigate to the screen where the expenses will be listed.
        binding.ExpensesButton.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            intent.putExtra("CURRENT_STATUS",binding.buttonChangeState.text as String)
            startActivity(intent)
        }

        //Configure the button to update the trip. Clicking the button updates the information in the DB and navigates to the screen with all the trips listed. If there is an error a Toast message will be displayed.
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

        //Configure the "Expenses" button to navigate to the screen where the participants of the trip will be listed.
        binding.gotoParticipantsButtonTripInformation.setOnClickListener {
            val intent = Intent(applicationContext, TripParticipantsActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            startActivity(intent)
        }

        //Configure the "Cancel" button to navigate to the previous screen without save any changes when the user presses it.
        binding.cancelButtonTripInformation.setOnClickListener{
            this.finish()
        }
    }
    /**
     * Disables the editing of trip information fields.
     */
    private fun disableChangeTripInformation() {
        binding.tripNameEditTextTripInformation.isEnabled = false
        binding.startDateTextViewTripInformation.isEnabled = false
        binding.endDateTextViewTripInformation.isEnabled = false
        binding.locationEditTextTripInformation.isEnabled = false
        binding.maxPartNumberEditTextTripInformation.isEnabled = false
        binding.tripDescriptionEditTextTripInformation.isEnabled = false
        binding.dateRangePickerButtonTripInformation.isEnabled = false
    }

    private fun disableAddExpensesAndParticipants(){
        binding.gotoParticipantsButtonTripInformation.isEnabled = false
    }
}

/**
 * Converts a date from one format to another.
 *
 * @param inputDate The input date string in the original format.
 * @return The date string converted to the desired format (yyyy-MM-dd).
 */
fun convertDate (inputDate: String): String {
    val inputFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

