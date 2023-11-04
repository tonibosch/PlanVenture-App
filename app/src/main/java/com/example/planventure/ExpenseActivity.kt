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
import com.example.planventure.databinding.ActivityAllExpenseBinding
import com.example.planventure.databinding.ActivityCreateExpenseBinding
import com.example.planventure.service.ExpenseService
import com.example.planventure.service.TripService
import com.example.planventure.utility.ExpenseAdapter

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAllExpenseBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    // Services
    private lateinit var expenseService: ExpenseService

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        expenseService = ExpenseService(applicationContext)
        expenseAdapter = ExpenseAdapter(expenseService.getAllExpenses(), applicationContext)

        binding.recyclerViewAllExpenses.adapter = expenseAdapter
        binding.recyclerViewAllExpenses.layoutManager = LinearLayoutManager(this)

        binding.backButtonExpenses.setOnClickListener{
            this.finish()
        }

        binding.addExpenseButton.setOnClickListener {
            val intent = Intent(this, CreateExpenseActivity::class.java)
            startActivity(intent)
        }
    }
}