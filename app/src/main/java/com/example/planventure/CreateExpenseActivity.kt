package com.example.planventure

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.Exception.MultipleNamesException
import com.example.planventure.databinding.ActivityCreateExpenseBinding
import com.example.planventure.service.ExpenseService
import com.example.planventure.service.ParticipantService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ExpenseParticipantAdapter

/**
 * An activity for creating new expenses related to a trip.
 *
 * This activity allows users to create new expenses associated with a specific trip. It provides
 * user interfaces for inputting expense details such as the expense name and amount.
 *
 * @property binding The binding object for the activity's layout.
 * @property expenseService The service responsible for managing expenses.
 * @property tripService The service responsible for managing trips.
 */
class CreateExpenseActivity : AppCompatActivity() {

    private lateinit var participants: ArrayList<String>

    private lateinit var binding: ActivityCreateExpenseBinding

    //Services
    private lateinit var expenseService: ExpenseService
    private lateinit var tripService: TripService
    private lateinit var participantService: ParticipantService

    //Adapter
    private lateinit var expenseParticipantAdapter: ExpenseParticipantAdapter

    /**
     * Initializes the activity's view and sets up UI components.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        expenseService = ExpenseService(applicationContext)
        tripService = TripService(applicationContext)
        participantService = ParticipantService(applicationContext)

        expenseParticipantAdapter =
            ExpenseParticipantAdapter(ArrayList())
        binding.ParticipantExpenseRV.adapter = expenseParticipantAdapter
        binding.ParticipantExpenseRV.layoutManager =
            LinearLayoutManager(applicationContext)

        participants = ArrayList()
        val tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)
        val trip = tripService.getTripById(tripId)
        if (trip != null) {
            participantService.getParticipantsByTrip(trip).forEach {
                participants.add(it.getName())
            }
        }

        //Define Spinner
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, participants)
        binding.ParticipantSpinner.adapter = spinnerAdapter
        binding.ParticipantSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    expenseParticipantAdapter.paidBy = participantService.getParticipantByName(
                        binding.ParticipantSpinner.selectedItem.toString(),
                        trip!!
                    )
                    val tripParticipants = participantService.getParticipantsByTrip(trip)

                    expenseParticipantAdapter.updateParticipants(tripParticipants)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        //Define buttons behavior

        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButtonCreateExpenses.setOnClickListener {
            this.finish()
        }

        binding.createExpenseButtonCreateExpenses.setOnClickListener {
            try {
                val name = binding.expenseNameEditText.text.toString()
                val amount = binding.expenseAmountEditText.text.toString()

                expenseService.addExpenseToDb(
                    name,
                    amount,
                    trip,
                    expenseParticipantAdapter.getCheckedParticipants(),
                    expenseParticipantAdapter.paidBy
                )

                setResult(RESULT_OK)
                intent = Intent(this, ExpenseActivity::class.java)
                intent.putExtra("TRIPID", tripId)
                startActivityForResult(intent,1)
            } catch (e: EmptyDataException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e:MultipleNamesException){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}