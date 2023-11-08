package com.example.planventure.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ExpenseRepository

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseService(applicationContext: Context) {


    private val expenseRepository = ExpenseRepository(applicationContext)

    fun getAllExpenses(): ArrayList<Expense> {
        return expenseRepository.findAll()
    }


    fun addExpenseToDb(expense: Expense, trip: Trip?) {
        if (trip != null) {
            expenseRepository.addToDB(Pair(expense,trip.getId().toInt()))
        }
    }

    fun getExpenseById(id: Long): Expense? {
        return expenseRepository.getById(id)
    }

    fun getExpenseId(): Int {
        return expenseRepository.findAll().size + 1
    }



}