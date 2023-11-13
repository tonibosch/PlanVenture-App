package com.example.planventure.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ExpenseRepository
import com.example.planventure.repository.ParticipantExpenseRepository

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseService(applicationContext: Context) {


    private val expenseRepository = ExpenseRepository(applicationContext)
    private val participantExpenseRepository = ParticipantExpenseRepository(applicationContext)
    fun getAllExpenses(): ArrayList<Expense> {
        return expenseRepository.findAll()
    }

    fun getAllExpensesById(id: Long): ArrayList<Expense> {
        return expenseRepository.getExpensesById(id)
    }


    fun addExpenseToDb(expense: Expense, trip: Trip?) {
        if (trip != null) {
            expenseRepository.addToDB(Pair(expense, trip.getId().toInt()))
        }
    }

    fun getExpenseById(id: Long): Expense? {
        return expenseRepository.getById(id)
    }

    fun getExpenseByName(eName: String, t: Trip): Expense {
        return expenseRepository.getExpensesByTrip(t).filter { it.getName() == eName }[0]

    }

    fun addExpenseParticipantsToDb(
        expense: Expense?,
        participants: ArrayList<Participant>,
        paidBy: Participant?
    ) {
        if (expense != null) {

            if (paidBy != null && !paidByContained(participants, paidBy)) {
                participants.add(paidBy)
            }

            participants.forEach {
                if (it.getName() == paidBy?.getName()) {
                    participantExpenseRepository.addToDB(
                        Pair(
                            Triple(
                                it.getId().toInt(),
                                expense.getId().toInt(),
                                expense.getAmount()
                            ), -1))
                } else {
                    participantExpenseRepository.addToDB(
                        Pair(
                            Triple(
                                it.getId().toInt(),
                                expense.getId().toInt(),
                                0f),
                            -1))
                }
            }
        }
    }

    private fun paidByContained(
        participants: ArrayList<Participant>,
        paidBy: Participant
    ): Boolean {
        participants.forEach {
            if (it.getId() == paidBy.getId()) {
                return true
            }
        }
        return false
    }
}



