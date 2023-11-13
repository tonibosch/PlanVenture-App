package com.example.planventure.entity

/**
 * Expense.kt
 * Value object that stores the attributes of an expense
 * @property id
 * @property name
 * @property amount
 *
 * @constructor (id: Long, name: String, amount: Float)
 *
 * @see Trip
 */
class Expense(
    /**
     * Id of the expense.
     * Used to identify the expense in the database
     */
    private var id: Long,
    /**
     * Attribute, how the expense is called.
     * To quickly identify in the UI
     */
    private var name: String,
    /**
     * cost of the expense
     */
    private var amount: Float
) {


    fun getId(): Long{return id}
    fun getName(): String{return name}
    fun getAmount(): Float{return amount}
}