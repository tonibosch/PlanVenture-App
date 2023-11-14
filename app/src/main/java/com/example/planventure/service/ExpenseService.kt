package com.example.planventure.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planventure.Exception.EmptyDataException
import com.example.planventure.entity.Expense
import com.example.planventure.entity.Participant
import com.example.planventure.entity.Trip
import com.example.planventure.repository.ExpenseRepository
import com.example.planventure.repository.ParticipantExpenseRepository
import com.example.planventure.repository.ParticipantParticipantRepository
import java.lang.Error

@RequiresApi(Build.VERSION_CODES.P)
class ExpenseService(applicationContext: Context) {


    private val expenseRepository = ExpenseRepository(applicationContext)
    private val participantExpenseRepository = ParticipantExpenseRepository(applicationContext)
    private val participantParticipantRepository =
        ParticipantParticipantRepository(applicationContext)

    fun getAllExpensesById(id: Long): ArrayList<Expense> {
        return expenseRepository.getExpensesById(id)
    }

    fun addExpenseToDb(name: String, amount: String, trip: Trip?) {
        try {
            if (trip != null) {
                checkEmptyValues(name, amount)
                val expense = Expense(-1, name, amount.toFloat())
                expenseRepository.addToDB(Pair(expense, trip.getId().toInt()))
            }
        } catch (e: Error) {
            throw e
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
        try {
            if (expense != null) {

                checkEmptyParticipants(participants, paidBy)
                var isInTrip = paidByContained(participants, paidBy!!)
                if (!isInTrip) {
                    participants.add(paidBy)
                    isInTrip = false
                    expenseRepository.storePayer(false, expense)
                }

                participants.forEach {
                    if (it.getName() == paidBy.getName()) {
                        participantExpenseRepository.addToDB(
                            Pair(
                                Triple(
                                    it.getId().toInt(),
                                    expense.getId().toInt(),
                                    expense.getAmount()
                                ), -1
                            )
                        )
                    } else {
                        participantExpenseRepository.addToDB(
                            Pair(
                                Triple(
                                    it.getId().toInt(),
                                    expense.getId().toInt(),
                                    0f
                                ),
                                -1
                            )
                        )
                    }
                }

                addDebtsToDB(participants, isInTrip, paidBy, expense)
            }
        } catch (e: Error) {
            throw e
        }
    }

    private fun addDebtsToDB(
        participants: ArrayList<Participant>,
        inTrip: Boolean,
        paidBy: Participant,
        expense: Expense
    ) {
        for (p in participants) {
            if (p.getId() == paidBy.getId()) continue

            participantParticipantRepository.addToDB(
                Pair(
                    Triple(
                        paidBy.getId().toInt(),
                        p.getId().toInt(),
                        expense.getAmount() / if(inTrip)participants.size else (participants.size - 1)
                    ),
                    -1
                )
            )
        }
    }


    fun getIfPayerParticipated(e: Expense): Boolean{
        return expenseRepository.getPayer(e)
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

    fun getTotal(tripId: Long): Float {
        var totalAmount = 0f
        getAllExpensesById(tripId).forEach {
            totalAmount += it.getAmount()
        }
        return totalAmount
    }

    private fun checkEmptyValues(name: String, amount: String) {
        if (name.isEmpty()) {
            throw EmptyDataException("Please enter a name")
        } else if (amount.isEmpty()) {
            throw EmptyDataException("Please enter an amount")
        }
    }

    private fun checkEmptyParticipants(p: ArrayList<Participant>, paidBy: Participant?) {
        if (p.isEmpty()) {
            throw EmptyDataException("Please check at least on participant")
        } else if (p.size == 1 && p[0].getId() == paidBy?.getId()) {
            throw EmptyDataException("Please check at least one participant other than the one who paid")
        }
    }
}





