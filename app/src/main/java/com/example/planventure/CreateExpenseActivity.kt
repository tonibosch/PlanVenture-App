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

class CreateExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateExpenseBinding
    private lateinit var expenseService: ExpenseService
    private lateinit var tripService: TripService

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