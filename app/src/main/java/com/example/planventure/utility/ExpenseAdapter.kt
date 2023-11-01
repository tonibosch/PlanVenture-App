package com.example.planventure.utility

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.ExpenseInformationActivity
import com.example.planventure.MyTripsFragment
import com.example.planventure.R
import com.example.planventure.entity.Expense

class ExpenseAdapter (
    private var expenses: ArrayList<Expense>,
    private val application: Context
    ): RecyclerView.Adapter<ExpenseAdapter.ExpensesViewHolder>() {


    companion object {
        const val EXPENSE_ID = "com.example.planventure.ExpensesAdapter.expenseId"
    }

        class ExpensesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        return ExpensesViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.fragment_each_expense,
                    parent,
                    false
                )
            )
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val currentExpense = expenses[position]

        holder.itemView.apply {

            findViewById<TextView>(R.id.expenseName).text =
                "Name: \t" + currentExpense.getName()

            findViewById<TextView>(R.id.expenseAmount).text =
                "Amount: \t" + currentExpense.getAmount().toString()

            this.setOnClickListener{
                val intent = Intent(this.context, ExpenseInformationActivity::class.java)
                intent.putExtra(EXPENSE_ID, currentExpense.getId())

                this.context.startActivity(intent)
            }
        }

    }

}