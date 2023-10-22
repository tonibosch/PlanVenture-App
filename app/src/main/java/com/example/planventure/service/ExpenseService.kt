package com.example.planventure.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ExpenseRepository
import com.example.planventure.repository.ParticipantRepository
@RequiresApi(Build.VERSION_CODES.P)
class ExpenseService(applicationContext: Context) {


    private val expenseRepository = ExpenseRepository(applicationContext)

    fun getSize(): Long {
        return expenseRepository.getSize()
    }

    fun addExpenseToDb(expense: Expense, trip: Trip?) {
        TODO("Not yet implemented")
    }


}