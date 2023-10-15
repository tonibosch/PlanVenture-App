package com.example.planventure.entity

class Expense(
    private var id: Long,
    private var name: String,
    private var amount: Float
) {


    fun getId(): Long{return id}
    fun getName(): String{return name}
    fun getAmount(): Float{return amount}
}