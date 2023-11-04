package com.example.planventure

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.databinding.ActivityCreateExpenseBinding
import com.example.planventure.databinding.ActivityCreateTripBinding
import com.example.planventure.entity.Expense
import com.example.planventure.service.ExpenseService
import com.example.planventure.service.TripService

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

    private lateinit var binding: ActivityCreateExpenseBinding
    //Services
    private lateinit var expenseService: ExpenseService
    private lateinit var tripService: TripService

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

        val tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)
        val trip = tripService.getTripById(tripId)

        //Define buttons behavior

        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButtonCreateExpenses.setOnClickListener{
            this.finish()
        }

        binding.createExpenseButtonCreateExpenses.setOnClickListener {
            Log.d("NEW EXPENSE", "Name: ${binding.expenseNameEditText.text}, Amount: ${binding.expenseAmountEditText.text}")
            this.finish()
            /**
            setResult(Activity.RESULT_OK)
            try {
                val expense: Expense =
                    Expense(expenseService.getSize() + 1, expenseName.text.toString(), expenseAmount.text.toString().toFloat())
                if (expense != null) {
                    expenseService.addExpenseToDb(expense, trip)
                }
                this.finish()
            } catch (e: EmptyDataException) { // catches Exception and makes toast out of it
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            */
        }
    }
}