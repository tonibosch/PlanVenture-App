package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MaxParticipantsOverflow
import com.example.planventure.databinding.ActivityCreateTripBinding
import com.example.planventure.service.TripService
import com.example.planventure.utility.DatePicker

class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    //Services
    private lateinit var tripService: TripService

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tripService = TripService(applicationContext)

        //Define buttons behavior
        //Configures the button to create a new trip. If there is no error, the trip is created and the user navigates to the screen with all trips listed. If there is an error a Toast message will be displayed.
        binding.saveButton.setOnClickListener {

            setResult(Activity.RESULT_OK)
            try {
                //creates list with data for the trip
                val myData = mutableListOf<String>()
                myData.add(binding.tripNameEditText.text.toString())
                myData.add(binding.startDateTextView.text.toString())
                myData.add(binding.endDateTextView.text.toString())
                myData.add(binding.locationEditText.text.toString())
                myData.add(binding.maxPartNumberEditText.text.toString())
                myData.add("Enter a description")
                tripService.addTrip(myData)
                this.finish()
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: MaxParticipantsOverflow){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        //Configure the "Cancel" button to navigate to the previous screen without save any changes when the user presses it.
        binding.cancelButton.setOnClickListener {
            this.finish()
        }

        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButton.setOnClickListener {
            this.finish()
        }

        //Configure the button to select the dates of the trip. Clicking the button opens a calendar for the user to select the dates.
        binding.dateRangePickerButton.setOnClickListener {
            val datePicker = DatePicker(supportFragmentManager,binding.startDateTextView, binding.endDateTextView)
            datePicker.showDateRangePicker()
        }
    }
}