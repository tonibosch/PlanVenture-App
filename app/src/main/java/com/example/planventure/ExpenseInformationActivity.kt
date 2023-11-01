package com.example.planventure

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.planventure.service.ExpenseService
import com.example.planventure.utility.ExpenseAdapter.Companion.EXPENSE_ID

class ExpenseInformationActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var expenseService: ExpenseService
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_information)

        expenseService = ExpenseService(this)
        val expenseId = intent.getLongExtra(EXPENSE_ID, 0)

        val expense = expenseService.getExpenseById(expenseId)

        textView = findViewById(R.id.textView11)
        textView.text = expense?.getName()

    }
}