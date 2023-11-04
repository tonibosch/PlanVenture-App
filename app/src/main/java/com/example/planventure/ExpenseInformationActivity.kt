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

/**
 * An activity for displaying information about a specific expense.
 *
 * This activity is responsible for displaying details about a particular expense, including its
 * name, amount, and other relevant information. It allows users to view information about a
 * selected expense.
 *
 * @property binding The binding object for the activity's layout.
 * @property expenseService The service responsible for managing expenses.
 */
class ExpenseInformationActivity : AppCompatActivity() {

    private lateinit var binding:ActivityExpenseInformationBinding
    //Services
    private lateinit var expenseService: ExpenseService

    /**
     * Initializes the activity's view and displays information about the selected expense.
     *
     * @param savedInstanceState The saved instance state, if any.
     */
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