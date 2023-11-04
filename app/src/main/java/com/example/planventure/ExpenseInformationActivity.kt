package com.example.planventure

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.planventure.databinding.ActivityAllExpenseBinding
import com.example.planventure.databinding.ActivityExpenseInformationBinding
import com.example.planventure.service.ExpenseService
import com.example.planventure.utility.ExpenseAdapter.Companion.EXPENSE_ID

class ExpenseInformationActivity : AppCompatActivity() {

    private lateinit var binding:ActivityExpenseInformationBinding
    private lateinit var expenseService: ExpenseService
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        expenseService = ExpenseService(this)
        val expenseId = intent.getLongExtra(EXPENSE_ID, 0)

        val expense = expenseService.getExpenseById(expenseId)
        binding.textView11.text = expense?.getName()

    }
}