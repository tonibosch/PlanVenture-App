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
import com.example.planventure.entity.Trip
import com.example.planventure.enumerations.TRIP_STATE
import com.example.planventure.service.ParicipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
class TripInformationActivity : AppCompatActivity() {

    companion object {
        const val TRIP_ID_TRIP_PARTICIPANTS = "com.example.planventure.TripInformation.TripId"
    }

    private lateinit var tripName: EditText
    private lateinit var description: EditText

    private lateinit var location: EditText
    private lateinit var maxNumberOfParts: EditText
    private lateinit var backButton: ImageButton
    private lateinit var dateRangePickerButton: Button
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var updateTripBtn: Button
    private lateinit var expensesBtn: Button
    private lateinit var changeStateBtn: Button

    private lateinit var gotoParticipants: Button

    private lateinit var tripService: TripService
    private lateinit var participantService: ParicipantService



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripService = TripService(applicationContext)
        participantService = ParicipantService(applicationContext)

        val tripId = intent.getLongExtra(MyTripsFragment.TRIP_ID_TRIP_INFORMATION,0)
        val trip = tripService.getTripById(tripId)

        setContentView(R.layout.activity_trip_information)


        backButton = findViewById(R.id.backButton_TripInfo)
        dateRangePickerButton = findViewById(R.id.dateRangePickerButton_tripInformation)
        tvStartDate = findViewById(R.id.startDate_textView_tripInformation)
        tvEndDate = findViewById(R.id.endDate_textView_tripInformation)
        tripName = findViewById(R.id.tripName_editText_tripInformation)
        description = findViewById(R.id.tripDescription_editText_tripInformation)

        location = findViewById(R.id.location_editText_tripInformation)
        maxNumberOfParts = findViewById(R.id.maxPartNumber_editText_tripInformation)
        updateTripBtn = findViewById(R.id.updateTripButton)
        backButton = findViewById(R.id.backButton_TripInfo)
        gotoParticipants = findViewById(R.id.gotoParticipants_Button_tripInformation)
        expensesBtn = findViewById(R.id.ExpensesButton)
        changeStateBtn = findViewById(R.id.buttonChangeState)

        tripName.text = Editable.Factory.getInstance().newEditable(trip?.getName())
        tvStartDate.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getStartDate().toString()))
        tvEndDate.text = Editable.Factory.getInstance().newEditable(convertDate(trip?.getEndDate().toString()))
        location.text = Editable.Factory.getInstance().newEditable(trip?.getLocation())
        maxNumberOfParts.text = Editable.Factory.getInstance().newEditable(trip?.getMaxNumberOfParticipants().toString())
        description.text = Editable.Factory.getInstance().newEditable(trip?.getDescription())

        backButton.setOnClickListener {
            this.finish()
        }

        val currentStatus = trip?.getState()
        Log.d("CURRENT STATUS", "${currentStatus.toString()}")

        when (currentStatus) {
            TRIP_STATE.PLANNING -> changeStateBtn.text = "START"
            TRIP_STATE.STARTED -> changeStateBtn.text = "FINISH"
            TRIP_STATE.FINISHED -> changeStateBtn.text = "FINISHED"
            else -> changeStateBtn.text = "ELSE"
        }

        changeStateBtn.setOnClickListener {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            if(currentStatus == TRIP_STATE.PLANNING) {
                changeStateBtn.text = "FINISH"
                tripService.updateTrip(tripId, Trip(1, tripName.text.toString(), formatter.parse(tvStartDate.text.toString()),
                    formatter.parse(tvEndDate.text.toString()), location.text.toString(), maxNumberOfParts.text.toString().toInt(),
                    description.text.toString(), ArrayList(), ArrayList(),TRIP_STATE.STARTED))
                Toast.makeText(this, "You have started the trip", Toast.LENGTH_SHORT).show()
            }
            else if(currentStatus == TRIP_STATE.STARTED){
                changeStateBtn.text = "FINISHED"
                tripService.updateTrip(tripId, Trip(1, tripName.text.toString(), formatter.parse(tvStartDate.text.toString()),
                    formatter.parse(tvEndDate.text.toString()), location.text.toString(), maxNumberOfParts.text.toString().toInt(),
                    description.text.toString(), ArrayList(), ArrayList(),TRIP_STATE.FINISHED))
                Toast.makeText(this, "You have finished the trip", Toast.LENGTH_SHORT).show()
            }
        }

        dateRangePickerButton.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager, tvStartDate, tvEndDate)
            datePicker.showDateRangePicker()
        }

        expensesBtn.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            startActivity(intent)
        }

        updateTripBtn.setOnClickListener {

            setResult(Activity.RESULT_OK)
            try {
                var tripState = tripService.getTripById(tripId)!!.getState()
                //println("DESCRIPTION" + description.text.toString())
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                tripService.updateTrip(
                    tripId, Trip(
                        1,
                        tripName.text.toString(),
                        formatter.parse(tvStartDate.text.toString()),
                        formatter.parse(tvEndDate.text.toString()),
                        location.text.toString(),
                        maxNumberOfParts.text.toString().toInt(),
                        description.text.toString(),
                        ArrayList(),
                        ArrayList(),
                        tripState
                    )
                )
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        gotoParticipants.setOnClickListener {
            val intent = Intent(applicationContext, TripParticipantsActivity::class.java)
            intent.putExtra(TRIP_ID_TRIP_PARTICIPANTS, tripId)
            startActivity(intent)
        }


    }
}

fun convertDate (inputDate: String): String {
    val inputFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val date = inputFormat.parse(inputDate)
    val formattedDate = outputFormat.format(date)
    return formattedDate
}
