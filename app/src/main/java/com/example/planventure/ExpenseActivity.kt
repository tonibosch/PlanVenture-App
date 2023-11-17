package com.example.planventure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planventure.CreateExpenseActivity.Companion.TRIP_ID_CREATE_EXPENSE
import com.example.planventure.databinding.ActivityAllExpenseBinding
import com.example.planventure.service.ExpenseService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ExpenseAdapter

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllExpenseBinding

    // Services
    private lateinit var expenseService: ExpenseService
    private lateinit var tripService: TripService

    //Adapters
    private lateinit var expenseAdapter: ExpenseAdapter

    //tripId
    private var tripId: Long = 0

    companion object {
        private const val CREATE_EXPENSE_REQUEST = 2 // You can choose any integer value
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tripService = TripService(applicationContext)

        tripId = intent.getLongExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, 0)
        val status = intent?.extras?.getString("CURRENT_STATUS").toString()

        val trip = tripService.getTripById(tripId)

        expenseService = ExpenseService(applicationContext)

        loadExpenseList()

        //Configure the "Back" button to navigate to the previous screen when the user presses it.
        binding.backButtonExpenses.setOnClickListener {
            this.finish()
        }

        if (status == "FINISHED") binding.addExpenseButton.isEnabled = false

        binding.addExpenseButton.setOnClickListener {
            if (trip?.getParticipants()!!.size >= 2) {
                val intent = Intent(this, CreateExpenseActivity::class.java)
                intent.putExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, tripId)
                startActivityForResult(intent, CREATE_EXPENSE_REQUEST)
            } else {
                Toast.makeText(this, "Please add at least 2 Participants", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.balanceButton.setOnClickListener {
            if (expenseService.getAllExpensesByTripId(tripId).size != 0) {
                val intent = Intent(this, BalanceActivity::class.java)
                intent.putExtra(TripInformationActivity.TRIP_ID_TRIP_PARTICIPANTS, tripId)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "You need at least one Expense to access the balance",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        val totalAmount = expenseService.getTotal(tripId)
        binding.totalAmount.text = "Total: ${String.format("%.2f", totalAmount)}€"

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_EXPENSE_REQUEST && resultCode == Activity.RESULT_OK)
            loadExpenseList()


    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun loadExpenseList() {
        expenseAdapter = ExpenseAdapter(expenseService.getAllExpensesByTripId(tripId))
        binding.recyclerViewAllExpenses.adapter = expenseAdapter
        binding.recyclerViewAllExpenses.layoutManager = LinearLayoutManager(this)
    }
}