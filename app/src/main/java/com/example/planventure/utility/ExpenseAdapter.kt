package com.example.planventure.utility

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.ExpenseInformationActivity
import com.example.planventure.R
import com.example.planventure.entity.Expense
import com.google.android.material.appbar.MaterialToolbar

class ExpenseAdapter (
    private var expenses: ArrayList<Expense>
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val currentExpense = expenses[position]

        holder.itemView.apply {

            findViewById<TextView>(R.id.expenseName).text =
                "Name: \t" + currentExpense.getName()

            findViewById<TextView>(R.id.expenseAmount).text =
                "Amount: \t${String.format("%.2f",currentExpense.getAmount())}€"

            // Make the expense clickable
            findViewById<MaterialToolbar>(R.id.materialToolbar)
                .setOnClickListener{
                val intent = Intent(this.context, ExpenseInformationActivity::class.java)
                intent.putExtra(EXPENSE_ID, currentExpense.getId())

                this.context.startActivity(intent)
            }
        }

    }

}