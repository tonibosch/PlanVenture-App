package com.example.planventure

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.service.ExpenseService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ExpenseAdapter

class ExpenseActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var addExpenseButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter


    // Services
    private lateinit var expenseService: ExpenseService

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_expense)

        backButton = findViewById(R.id.backButton_Expenses)

        expenseService = ExpenseService(applicationContext)
        expenseAdapter = ExpenseAdapter(expenseService.getAllExpenses(), applicationContext)


        recyclerView = findViewById(R.id.recyclerView_all_expenses)

        recyclerView.adapter = expenseAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        backButton.setOnClickListener{
            this.finish()
        }

        addExpenseButton = findViewById(R.id.addExpenseButton)

        addExpenseButton.setOnClickListener {
            val intent = Intent(this, CreateExpenseActivity::class.java)
            startActivity(intent)
        }



    }
}