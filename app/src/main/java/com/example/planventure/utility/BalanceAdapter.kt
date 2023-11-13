package com.example.planventure.utility

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R

class BalanceAdapter(
    private var debts: ArrayList<Triple<String,String,Float>>,
    private val applicationContext: Context,

) : RecyclerView.Adapter<BalanceAdapter.BalancesViewHolder>(){
    class BalancesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalancesViewHolder {
        return BalanceAdapter.BalancesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_each_expense,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return debts.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BalancesViewHolder, position: Int) {
        val currentDebt = debts[position]

        holder.itemView.apply{
            //ExpenseName represents the who owes who
            findViewById<TextView>(R.id.expenseName).text =
                "${currentDebt.second} owes ${currentDebt.first}:"

            //ExpenseAmount represents how much is owed
            findViewById<TextView>(R.id.expenseAmount).text =
                "${String.format("%.2f", currentDebt.third)}€"

        }
    }
}